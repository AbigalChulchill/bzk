import { async } from '@angular/core/testing';
import { PageDto } from './../dto/page-dto';
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
  public list = new PageDto<RunLog>();
  public runState: RunState = null;
  public page = 0;
  public pages = new Array<number>();
  public size = 10;

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
      this.list = await this.runLogClient.list(this.queryUid, this.listType, this.page, this.size).toPromise();
      this.setupPage();
    } catch (e) {

    }
    this.loading.dismiss(t);
  }

  public onPageChange(p: number): void {
    this.page = p;
    this.reflesh();
  }

  public onSizeChange(s:number):void{
    this.size = s;
    this.page = 0;
    this.reflesh();
  }

  public setupPage(): void {
    this.pages = new Array<number>();
    for (let i = 0; i < this.list.totalPages; i++) {
      this.pages.push(i);
    }
  }

  public listLogs(): Array<RunLog> {
    if (!this.runState)
      return this.list.content;
    return this.list.content.filter(rl => rl.state == this.runState);
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

