import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Constant } from '../infrastructure/constant';
import { Flow } from '../model/flow';
import { SavedFlow } from '../model/saved-flow';
import { BzkUtils } from '../utils/bzk-utils';
import { CommUtils } from '../utils/comm-utils';

@Injectable({
  providedIn: 'root'
})
export class JobClientService {


  public static URL_PREFIX = 'job/';

  constructor(
    private httpClient: HttpClient
  ) { }

  public async listAll(): Promise<Array<SavedFlow>> {
    const resp = await this.httpClient.get<Array<SavedFlow>>(environment.apiHost + JobClientService.URL_PREFIX + 'all').toPromise();
    resp.forEach(sf => sf.model = BzkUtils.fitClzz(Flow, sf.model));
    return resp;
  }

  public async getByUid(uid: string): Promise<SavedFlow> {
    const resp = await this.httpClient.get<SavedFlow>(environment.apiHost + JobClientService.URL_PREFIX + uid).toPromise();
    resp.model = BzkUtils.fitClzz(Flow, resp.model);
    return resp;
  }

  public remove(uid: string): Observable<void> {
    return this.httpClient.delete<void>(environment.apiHost + JobClientService.URL_PREFIX + uid);
  }

  public save(m: Flow): Observable<SavedFlow> {
    return this.httpClient.post<SavedFlow>(environment.apiHost + JobClientService.URL_PREFIX + 'save', m);
  }

  public async create(f: Flow): Promise<SavedFlow> {
    f.uid = CommUtils.makeAlphanumeric(Constant.UID_SIZE);
    const ans = await this.save(f).toPromise();
    return ans;
  }
}