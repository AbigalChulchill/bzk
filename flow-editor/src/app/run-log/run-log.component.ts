import { StringUtils } from './../utils/string-utils';
import { RunState } from './../model/pojo/enums';
import { LoadingService } from 'src/app/service/loading.service';
import { ListLogType, RunLogClientService } from './../service/run-log-client.service';
import { Component, OnInit } from '@angular/core';
import { RunLog } from '../model/run-log';
import { ActivatedRoute } from '@angular/router';
import { UrlParamsService } from '../service/url-params.service';

@Component({
  selector: 'app-run-log',
  templateUrl: './run-log.component.html',
  styleUrls: ['./run-log.component.css']
})
export class RunLogComponent implements OnInit {
  StringUtils = StringUtils;
  public showType = ShowType.Vars;
  public listType = ListLogType.runflow;
  public queryUid = '';
  // public jobUid = ''
  public list = new Array<RunLog>();
  public runState: RunState = null;

  constructor(
    private runLogClient: RunLogClientService,
    private loading: LoadingService,
    private urlParams: UrlParamsService,
    private route: ActivatedRoute
  ) { }

  async ngOnInit(): Promise<void> {
    if (StringUtils.isBlank(this.queryUid)) {
      this.queryUid = this.urlParams.getQueryUid();
    }
    this.listType = this.urlParams.getListType();

    this.reflesh();
  }



  public async reflesh(a = () => { }): Promise<void> {
    const t = this.loading.show();
    await a();
    try {
      this.list = await this.runLogClient.list(this.queryUid, this.listType).toPromise();
    } catch (e) {

    }
    this.loading.dismiss(t);
  }

  public listLogs(): Array<RunLog> {
    if (!this.runState)
      return this.list;
    return this.list.filter(rl => rl.state == this.runState);
  }

  public listRunState(): Array<RunState> {
    const ans = Object.values(RunState);
    ans.push(null);
    return ans;
  }

  public setRunState(rs: RunState): void {
    this.runState = rs;
  }

}

export enum ShowType {
  Vars, OrgJson, Message
}

