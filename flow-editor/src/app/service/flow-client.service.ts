import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { FlowPoolInfo, RunInfo } from '../dto/flow-pool-info';
import { Flow } from '../model/flow';
import { BzkUtils } from '../utils/bzk-utils';

@Injectable({
  providedIn: 'root'
})
export class FlowClientService {

  public static URL_PREFIX = 'flow/';

  constructor(
    private httpClient: HttpClient
  ) { }

  public async getDemoModel(): Promise<Flow> {
    const ans = await this.httpClient.get<Flow>(environment.apiHost + FlowClientService.URL_PREFIX + 'demo').toPromise();
    return BzkUtils.fitClzz(Flow, ans);
  }

  public debugAction(fuid: string, auid: string, delDelay: number): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + FlowClientService.URL_PREFIX + `${fuid}/debug/action/${auid}?delDelay=${delDelay}`, null);
  }

  public registerFlow(f: Flow[]): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + FlowClientService.URL_PREFIX + 'register', f);
  }

  public registerFlowByUid(fuid: string): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + FlowClientService.URL_PREFIX + fuid + '/register', null);
  }

  public runManual(fuid: string): Observable<RunInfo> {
    return this.httpClient.post<RunInfo>(environment.apiHost + FlowClientService.URL_PREFIX + `${fuid}/run?type=manual`, null);
  }

  public testFlow(eUid: string): Observable<RunInfo> {
    return this.httpClient.post<RunInfo>(environment.apiHost + FlowClientService.URL_PREFIX + `${eUid}/test`, null);
  }

  public listFlowPoolInfo(): Observable<Array<FlowPoolInfo>> {
    return this.httpClient.get<Array<FlowPoolInfo>>(environment.apiHost + FlowClientService.URL_PREFIX);
  }

  public getFlowPoolInfo(uid: string): Observable<FlowPoolInfo> {
    return this.httpClient.get<FlowPoolInfo>(environment.apiHost + FlowClientService.URL_PREFIX + uid);
  }

  public listArchiveRunInfo(uid: string): Observable<Array<RunInfo>> {
    return this.httpClient.get<Array<RunInfo>>(environment.apiHost + FlowClientService.URL_PREFIX + 'archive/runinfo/' + uid);
  }

  public forceRemovePool(uid: string): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + FlowClientService.URL_PREFIX + 'pool/' + uid + '/remove?type=force', null);
  }

  public reloadPool(uid: string): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + FlowClientService.URL_PREFIX + 'pool/' + uid + '/reload', null);
  }


}
