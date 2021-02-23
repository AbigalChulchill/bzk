import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RunLogClientService {
  public static URL_PREFIX = 'run/log/';

  constructor(
    private httpClient: HttpClient
  ) { }

  // public listByRunFlowUid(d: ActionDebugData, delDelay: number): Observable<List<RunLog>> {
  //   return this.httpClient.post<void>(environment.apiHost + FlowClientService.URL_PREFIX + 'debug/action?delDelay=' + delDelay, d);
  // }

}
