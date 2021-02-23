import { StringUtils } from 'src/app/utils/string-utils';
import { LoadingService } from 'src/app/service/loading.service';
import { RunLogClientService } from './../service/run-log-client.service';
import { Component, OnInit } from '@angular/core';
import { RunLog } from '../model/run-log';

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
    private loading: LoadingService
  ) { }

  async ngOnInit(): Promise<void> {
    const t = this.loading.show();
    this.list = await this.runLogClient.listByRunFlowUid(this.runFlowUid).toPromise();
  }

}

export enum ShowType {
  Vars, OrgJson, Message
}

