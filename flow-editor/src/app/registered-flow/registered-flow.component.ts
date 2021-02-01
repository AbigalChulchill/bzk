import { FlowClientService } from './../service/flow-client.service';
import { LoadingService } from './../service/loading.service';
import { FlowPoolInfo } from './../dto/flow-pool-info';
import { Component, OnInit } from '@angular/core';
import { HttpClientService } from '../service/http-client.service';

@Component({
  selector: 'app-registered-flow',
  templateUrl: './registered-flow.component.html',
  styleUrls: ['./registered-flow.component.css']
})
export class RegisteredFlowComponent implements OnInit {

  public list: Array<FlowPoolInfo>;
  // panelOpenState = false;
  constructor(
    private httpClient: HttpClientService,
    private loading: LoadingService,
    private flowClient:FlowClientService
  ) { }

  async ngOnInit(): Promise<void> {
    await this.reflesh();
  }

  private async reflesh(): Promise<void> {
    const t = this.loading.show();
    this.list = await this.flowClient.listFlowPoolInfo().toPromise();
    this.loading.dismiss(t);

  }

  public async forceRemovePool(uid: string): Promise<void> {
    await this.flowClient.forceRemovePool(uid).toPromise();
    await this.reflesh();
  }

}
