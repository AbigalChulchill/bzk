import { Observable } from 'rxjs';
import { VarCfg } from './../model/var-cfg';
import { BaseVar } from './../infrastructure/meta';
import { plainToClass } from 'class-transformer';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VarCfgClientService {

  public static URL_PREFIX = 'varcfg/';

  constructor(
    private httpClient: HttpClient
  ) { }


  public async listAll(): Promise<Array<VarCfg>> {
    const resp = await this.httpClient.get<Array<VarCfg>>(environment.apiHost + VarCfgClientService.URL_PREFIX + 'all').toPromise();
    resp.forEach(sf => sf.content = plainToClass(BaseVar, sf.content));
    return resp;
  }

  public async save(c: VarCfg): Promise<VarCfg> {
    const resp = await this.httpClient.post<VarCfg>(environment.apiHost + VarCfgClientService.URL_PREFIX + 'save', c).toPromise();
    resp.content = plainToClass(BaseVar, resp.content);
    return resp;
  }


  public  remove(uid: string): Observable<void> {
    return this.httpClient.delete<void>(environment.apiHost + VarCfgClientService.URL_PREFIX + uid);
  }

}
