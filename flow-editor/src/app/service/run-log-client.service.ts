import { RunLog } from './../model/run-log';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RunLogClientService {
  public static URL_PREFIX = 'run/log/';

  constructor(
    private httpClient: HttpClient
  ) { }

  public listByRunFlowUid(uid:string): Observable<Array<RunLog>> {
    return this.httpClient.get<Array<RunLog>>(environment.apiHost + RunLogClientService.URL_PREFIX + uid+'?type=runflow' );
  }

}
