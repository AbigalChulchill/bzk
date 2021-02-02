import { SavedFlowClientService } from './../service/saved-flow-client.service';
import { FlowClientService } from './../service/flow-client.service';
import { LoadingService } from './../service/loading.service';
import { FlowPoolInfo, FlowState } from './../dto/flow-pool-info';
import { Component, OnInit } from '@angular/core';
import { HttpClientService } from '../service/http-client.service';
import { SavedFlow } from '../model/saved-flow';

@Component({
  selector: 'app-registered-flow',
  templateUrl: './registered-flow.component.html',
  styleUrls: ['./registered-flow.component.css']
})
export class RegisteredFlowComponent implements OnInit {
  public FlowState = FlowState;
  public flowPoolInfos = new Array<FlowPoolInfo>();
  public savedFlows: Array<SavedFlow>;
  // panelOpenState = false;
  constructor(
    private loading: LoadingService,
    private flowClient: FlowClientService,
    private savedFlowClient: SavedFlowClientService
  ) { }

  async ngOnInit(): Promise<void> {
    await this.reflesh();
  }

  private async reflesh(): Promise<void> {
    const t = this.loading.show();
    this.savedFlows = await this.savedFlowClient.listAll().toPromise();
    this.loading.dismiss(t);
    this.flowPoolInfos = await this.flowClient.listFlowPoolInfo().toPromise();
  }

  public getPoolInfo(sf: SavedFlow): FlowPoolInfo {
    return this.flowPoolInfos.find(fp=> fp.flow.uid === sf.uid);
  }

  public getAllRunCount(sf: SavedFlow): number {
    const ri= this.flowPoolInfos.find(fp=> fp.flow.uid === sf.uid);
    if(!ri) return 0;
    return ri.runInfos.length;
  }

  public getStateCount(sf: SavedFlow,st:FlowState): number {
    const ri= this.flowPoolInfos.find(fp=> fp.flow.uid === sf.uid);
    if(!ri) return 0;
    return ri.runInfos.filter(r=> r.state === st).length;
  }

  public async forceRemovePool(uid: string): Promise<void> {
    await this.flowClient.forceRemovePool(uid).toPromise();
    await this.reflesh();
  }

}
