import { VarCfgService } from './var-cfg.service';
import { CommUtils } from 'src/app/utils/comm-utils';
import { StringUtils } from './../utils/string-utils';
import { Injectable } from '@angular/core';
import { ModelUpdateAdapter } from '../model/service/model-update-adapter';
import { VarUtils } from '../utils/var-utils';
import { ModifyingFlowService } from './modifying-flow.service';

@Injectable({
  providedIn: 'root'
})
export class VarService {

  private static instance: VarService;

  constructor(
    private varCfgService: VarCfgService
  ) {
    VarService.instance = this;
  }


  public listAllKeys(): Array<string> {
    const list = VarUtils.listVarKeyByFlow(ModelUpdateAdapter.getInstance().getFlow());
    const ans = new Array<string>();
    const includeKeys = this.varCfgService.listKeys(ModelUpdateAdapter.getInstance().getFlow());
    for(const k of includeKeys){
      CommUtils.pushUnique(ans, k);
    }
    list.forEach(s => {
      if (!StringUtils.isBlank(s)) CommUtils.pushUnique(ans, s);
    });
    return ans;
  }

  public static getInstance(): VarService { return VarService.instance; }

}
