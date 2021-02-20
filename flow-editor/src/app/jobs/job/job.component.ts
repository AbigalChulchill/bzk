import { Component, OnInit } from '@angular/core';
import { FlowPoolInfo, FlowState, RunInfo } from 'src/app/dto/flow-pool-info';
import { FlowClientService } from 'src/app/service/flow-client.service';

@Component({
  selector: 'app-job',
  templateUrl: './job.component.html',
  styleUrls: ['./job.component.css']
})
export class JobComponent implements OnInit {

  public uid = '';
  public flowPoolInfo: FlowPoolInfo;

  constructor(
    private flowClient: FlowClientService
  ) { }

  async ngOnInit(): Promise<void> {
    this.flowPoolInfo = await this.flowClient.getFlowPoolInfo(this.uid).toPromise();
  }

  private genRow(ri:RunInfo):Row{
    const ans = new Row();
    ans.runUid = ri.uid;
    ans.state = ri.state;
     ans.endTag = ri. TODO
    return ans;
  }

}

export class Row {
  public runUid: string;
  public triggerAt: string;
  public state:FlowState;
  public endTag:string;
}
