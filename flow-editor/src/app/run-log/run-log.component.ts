import { StringUtils } from 'src/app/utils/string-utils';
import { LoadingService } from 'src/app/service/loading.service';
import { RunLogClientService } from './../service/run-log-client.service';
import { Component, OnInit } from '@angular/core';
import { RunLog } from '../model/run-log';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-run-log',
  templateUrl: './run-log.component.html',
  styleUrls: ['./run-log.component.css']
})
export class RunLogComponent implements OnInit {
  StringUtils = StringUtils;
  public showType = ShowType.Vars;
  public runFlowUid = '';
  public list = new Array<RunLog>();

  constructor(
    private runLogClient: RunLogClientService,
    private loading: LoadingService,
    private route: ActivatedRoute
  ) { }

  async ngOnInit(): Promise<void> {
    this.runFlowUid = this.route.snapshot.paramMap.get('runFlowUid');
    this.reflesh();
  }

  public async reflesh(a = () => { }): Promise<void> {
    const t = this.loading.show();
    await a();
    try {
      this.list = await this.runLogClient.listByRunFlowUid(this.runFlowUid).toPromise();
    } catch (e) {

    }
    this.loading.dismiss(t);
  }

}

export enum ShowType {
  Vars, OrgJson, Message
}

