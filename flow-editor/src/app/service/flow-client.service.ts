import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ActionDebugData } from '../dto/debug-dtos';
import { FlowPoolInfo } from '../dto/flow-pool-info';
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

  public debugAction(d: ActionDebugData, delDelay: number): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + FlowClientService.URL_PREFIX + 'debug/action?delDelay=' + delDelay, d);
  }

  public registerFlow(f: Flow[]): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + FlowClientService.URL_PREFIX + 'register', f);
  }

  public registerFlowByUid(fuid: string): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + FlowClientService.URL_PREFIX + fuid+'/register',null);
  }

  public testFlow(eUid: string): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + FlowClientService.URL_PREFIX + `${eUid}/test`,null);
  }

  public listFlowPoolInfo(): Observable<Array<FlowPoolInfo>> {
    return this.httpClient.get<Array<FlowPoolInfo>>(environment.apiHost + FlowClientService.URL_PREFIX);
  }

  public getFlowPoolInfo(uid: string): Observable<FlowPoolInfo> {
    return this.httpClient.get<FlowPoolInfo>(environment.apiHost + FlowClientService.URL_PREFIX+uid);
  }

  public forceRemovePool(uid: string): Observable<void> {
    return this.httpClient.post<void>(environment.apiHost + FlowClientService.URL_PREFIX + 'pool/' + uid + '/remove?type=force', null);
  }


}
