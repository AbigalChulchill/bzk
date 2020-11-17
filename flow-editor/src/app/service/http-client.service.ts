import { ActionDebugData } from './../dto/debug-dtos';
import { ReadingInfo } from './../dto/console-dtos';
import { RegisteredFlow } from './../dto/registered-flow';
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
    const ans = await this.httpClient.get<Flow>(environment.apiHost + '/flow/demo').toPromise();
    return BzkUtils.fitClzz(Flow, ans);
  }

  public debugAction(d: ActionDebugData, delDelay: number): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + '/flow/debug/action?delDelay=' + delDelay, d);
  }

  public registerFlow(f: Flow): Observable<Flow> {
    return this.httpClient.post<Flow>(environment.apiHost + '/flow/register', f);
  }

  public listRegisters(): Observable<Array<RegisteredFlow>> {
    return this.httpClient.get<Array<RegisteredFlow>>(environment.apiHost + '/flow/registers');
  }

  public forceRemove(fuid: string): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + `/flow/${fuid}/remove?type=force`, null);
  }

  public createGits(p: boolean, desc: string, fs: GistFile[]): Observable<Gist> {
    const data = {
      description: desc,
      public: p,
      files: {}
    };
    fs.forEach(f => {
      const fo = {
        content: f.content,
        type: 'application/json'
      };
      data.files[f.filename] = fo;
    });
    return this.httpClient.post<Gist>(environment.githubHost + 'gists', data);
  }

  public updateGist(id: string, fs: GistFile[]): Observable<Gist> {
    const data = {
      description: 'BZK Update',
      public: false,
      files: {}
    };
    fs.forEach(f => {
      const fo = {
        content: f.content,
        type: 'application/json'
      };
      data.files[f.filename] = fo;
    });
    return this.httpClient.patch<Gist>(environment.githubHost + 'gists/' + id, data);
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

  public async getGist(id: string): Promise<Gist> {
    const res = await this.httpClient.get<Gist>(environment.githubHost + 'gists/' + id).toPromise();
    const ans = plainToClass(Gist, res);
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

}


