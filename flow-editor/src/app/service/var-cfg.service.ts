import { Injectable } from '@angular/core';
import { Flow } from '../model/flow';
import { VarCfg } from '../model/var-cfg';
import { VarCfgClientService } from './var-cfg-client.service';

@Injectable({
  providedIn: 'root'
})
export class VarCfgService {

  public state = FetchState.Idel;
  public list = new Array<VarCfg>();

  constructor(
    private varCfgClient: VarCfgClientService
  ) {
    this.reflesh();
  }


  public createEmpty(): void {
    this.list.push(new VarCfg());
  }

  public async reflesh(): Promise<void> {
    this.list = new Array<VarCfg>();
    this.state = FetchState.Loading;
    this.list = await this.varCfgClient.listAll();
    this.state = FetchState.Fetched;
  }


  public async updateToServer(v: VarCfg): Promise<void> {
    this.list = new Array<VarCfg>();
    this.state = FetchState.Loading;
    await this.varCfgClient.save(v);
    await this.reflesh();
  }

  public async removeToServer(uid: string): Promise<void> {
    this.list = new Array<VarCfg>();
    this.state = FetchState.Loading;
    await this.varCfgClient.remove(uid).toPromise();
    await this.reflesh();
  }

  public get(uid: string): VarCfg {
    return this.list.find(c => c.name === uid);
  }

  public listKeys(flow:Flow):Array<string>{
    const ans = new Array<string>();
    for(const cname of flow.varCfgNames){
      const vc = this.get(cname);
      vc.content.listFlatKV().forEach(kv=> ans.push(kv.key));
    }
    return ans;
  }



}

export enum FetchState {
  Idel = 'Idel', Loading = 'Loading', Fetched = 'Fetched'
}
