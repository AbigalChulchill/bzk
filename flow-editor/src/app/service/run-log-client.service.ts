import { PageDto } from './../dto/page-dto';
import { RunLog } from './../model/run-log';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

export enum ListLogType{
  runflow = 'runflow',
  action = 'action'
}


@Injectable({
  providedIn: 'root'
})
export class RunLogClientService {
  public static URL_PREFIX = 'run/log/';

  constructor(
    private httpClient: HttpClient
  ) { }


  public list(uid:string,type:ListLogType,page:number,size:number): Observable<PageDto<RunLog>>{
    return this.httpClient.get<PageDto<RunLog>>(`${environment.apiHost}${RunLogClientService.URL_PREFIX}${uid}?type=${type}&page=${page}&size=${size}`);
  }

  public getByRefRunFlowUid(uid:string):Observable<RunLog>{
    return this.httpClient.get<RunLog>(`${environment.apiHost}${RunLogClientService.URL_PREFIX}${uid}?type=refRunFlowUid`);
  }

  // public listByRunFlowUid(uid:string): Observable<Array<RunLog>> {
  //   return this.httpClient.get<Array<RunLog>>(environment.apiHost + RunLogClientService.URL_PREFIX + uid+'?type=runflow' );
  // }

  // public listByActionUid(uid:string): Observable<Array<RunLog>> {
  //   return this.httpClient.get<Array<RunLog>>(environment.apiHost + RunLogClientService.URL_PREFIX + uid+'?type=action' );
  // }

}
