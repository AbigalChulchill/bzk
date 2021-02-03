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
    private modifyingFlow: ModifyingFlowService
  ) {
    VarService.instance = this;
  }


  public listAllKeys(): Array<string> {
    const list = VarUtils.listVarKeyByFlow(ModelUpdateAdapter.getInstance().getFlow());
    // TODO  const kvs = this.modifyingFlow.varsStore.listAllFlatKV();
    const ans = new Array<string>();
    list.forEach(s => {
      if (!StringUtils.isBlank(s)) CommUtils.pushUnique(ans, s);
    });
    return ans;
  }

  public static getInstance(): VarService { return VarService.instance; }

}
