import { FlowPoolInfo } from './../dto/flow-pool-info';
import { InPlain, SecretResult } from './../dto/secret-dtos';
import { ActionDebugData } from './../dto/debug-dtos';
import { ReadingInfo } from './../dto/console-dtos';
import { BzkUtils } from './../utils/bzk-utils';
import { async } from '@angular/core/testing';
import { Gist, GistFile } from './../dto/gist';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Flow } from '../model/flow';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { plainToClass } from 'class-transformer';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

  constructor(
    private httpClient: HttpClient
  ) { }

  public async getDemoModel(): Promise<Flow> {
    const ans = await this.httpClient.get<Flow>(environment.apiHost + 'flow/demo').toPromise();
    return BzkUtils.fitClzz(Flow, ans);
  }

  public debugAction(d: ActionDebugData, delDelay: number): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + 'flow/debug/action?delDelay=' + delDelay, d);
  }

  public registerFlow(f: Flow[]): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + 'flow/register', f);
  }

  public testFlow(eUid: string, f: Flow[]): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + `flow/${eUid}/test`, f);
  }

  public listFlowPoolInfo(): Observable<Array<FlowPoolInfo>> {
    return this.httpClient.get<Array<FlowPoolInfo>>(environment.apiHost + 'flow/');
  }

  public forceRemovePool(uid: string): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + 'flow/pool/' + uid + '/remove?type=force', null);
  }


  public async createGits(p: boolean, ePass: string, desc: string, fs: GistFile[]): Promise<Gist> {
    const data = {
      description: desc,
      public: p,
      files: {}
    };
    for (const f of fs) {
      const er = await this.encrypt({
        name: f.filename,
        plain: f.content,
        passHash: ePass
      }).toPromise();
      const fo = {
        content: er.data,
        type: 'application/json'
      };
      data.files[f.filename] = fo;
    }
    const ans = await this.httpClient.post<Gist>(environment.githubHost + 'gists', data).toPromise();
    return ans;
  }

  public async updateGist(id: string, ePass: string, fs: GistFile[]): Promise<Gist> {
    const data = {
      description: 'BZK Update',
      public: false,
      files: {}
    };
    for (const f of fs) {
      const er = await this.encrypt({
        name: f.filename,
        plain: f.content,
        passHash: ePass
      }).toPromise();
      const fo = {
        content: er.data,
        type: 'application/json'
      };
      data.files[f.filename] = fo;
    }
    const ans = await this.httpClient.patch<Gist>(environment.githubHost + 'gists/' + id, data).toPromise();
    return ans;
  }

  public deleteGist(id: string): Observable<void> {
    return this.httpClient.delete<void>(environment.githubHost + 'gists/' + id);
  }

  public async listGits(): Promise<Array<Gist>> {
    const res = await this.httpClient.get<Array<Gist>>(environment.githubHost + 'gists').toPromise();
    const ans = new Array<Gist>();
    for (const go of res) {
      const cg = plainToClass(Gist, go);
      ans.push(cg);
    }
    return ans;
  }

  public async getGist(id: string, ePass: string): Promise<Gist> {
    const res = await this.httpClient.get<Gist>(environment.githubHost + 'gists/' + id).toPromise();
    const ans = plainToClass(Gist, res);
    for (const fk of ans.files.keys()) {
      const f = ans.files.get(fk);
      const der = await this.decrypt({
        name: f.filename,
        plain: f.content,
        passHash: ePass
      }).toPromise();
      f.content = der.data;
    }
    return ans;
  }

  public isTralKeepReading(): Observable<ReadingInfo> {
    return this.httpClient.get<ReadingInfo>(environment.console.host + 'tail/reading');
  }

  public stopTralKeepReading(): Observable<void> {
    return this.httpClient.post<void>(environment.console.host + 'tail/stop', null);
  }

  public clearTrailFile(): Observable<void> {
    return this.httpClient.post<void>(environment.console.host + 'tail/clear', null);
  }

  public encrypt(ip: InPlain): Observable<SecretResult> {
    return this.httpClient.post<SecretResult>(environment.gistHost + 'secret/encrypt', ip);
  }

  public decrypt(ip: InPlain): Observable<SecretResult> {
    return this.httpClient.post<SecretResult>(environment.gistHost + 'secret/decrypt', ip);
  }

}


