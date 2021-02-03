import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SavedFlow } from '../model/saved-flow';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Flow } from '../model/flow';
@Injectable({
  providedIn: 'root'
})
export class SavedFlowClientService {

  public static URL_PREFIX = 'saved/flow/';

  constructor(
    private httpClient: HttpClient
  ) { }

  public listAll(): Observable<Array<SavedFlow>> {
    return this.httpClient.get<Array<SavedFlow>>(environment.apiHost + SavedFlowClientService.URL_PREFIX + 'all');
  }

  public getByUid(uid: string): Observable<SavedFlow> {
    return this.httpClient.get<SavedFlow>(environment.apiHost + SavedFlowClientService.URL_PREFIX + uid);
  }

  public remove(uid: string): Observable<void> {
    return this.httpClient.delete<void>(environment.apiHost + SavedFlowClientService.URL_PREFIX + uid);
  }

  public save(m: Flow): Observable<SavedFlow> {
    return this.httpClient.post<SavedFlow>(environment.apiHost + SavedFlowClientService.URL_PREFIX+'save', m);
  }


}
