import { JobClientService } from './../../service/job-client.service';
import { DialogService } from './../../uikit/dialog.service';
import { UrlParamsService } from './../../service/url-params.service';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { FlowPoolInfo, FlowState, RunInfo } from 'src/app/dto/flow-pool-info';
import { FlowClientService } from 'src/app/service/flow-client.service';
import { LoadingService } from 'src/app/service/loading.service';
import { ActivatedRoute } from '@angular/router';
import { ModifyingFlowService } from 'src/app/service/modifying-flow.service';
import { ReadJsonProvide } from 'src/app/uikit/json-editor/json-editor.component';
import { JobRunInfo } from 'src/app/dto/job-dto';

@Component({
  selector: 'app-job',
  templateUrl: './job.component.html',
  styleUrls: ['./job.component.css']
})
export class JobComponent implements OnInit, AfterViewInit {

  ReadJsonProvide = ReadJsonProvide;

  displayedColumns: string[] = ['runUid', 'startAt', 'endAt', 'state', 'endTag', 'actions'];
  dataSource: MatTableDataSource<Row>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  public uid = '';
  public flowPoolInfo: FlowPoolInfo;
  public jobRunInfo: JobRunInfo;

  constructor(
    private loading: LoadingService,
    private flowClient: FlowClientService,
    private jobClient: JobClientService,
    private route: ActivatedRoute,
    public dialogService: DialogService

  ) {
    this.dataSource = new MatTableDataSource(new Array<Row>());
  }


  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  async ngOnInit(): Promise<void> {
    this.uid = this.route.snapshot.paramMap.get('uid');
    this.jobRunInfo = await this.jobClient.getInfo(this.uid);
    await this.reflesh();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  public async reflesh(): Promise<void> {
    const t = this.loading.show();
    try {
      this.flowPoolInfo = await this.flowClient.getFlowPoolInfo(this.uid).toPromise();
      this.dataSource.data = this.genRows();
    } catch (e) {

    }
    this.loading.dismiss(t);
  }

  private genRows(): Array<Row> {
    const ans = new Array<Row>();
    if (!this.flowPoolInfo || !this.flowPoolInfo.runInfos) return ans;
    for (const ri of this.flowPoolInfo.runInfos) {
      ans.push(this.genRow(ri));
    }
    return ans;
  }

  private genRow(ri: RunInfo): Row {
    const ans = new Row();
    ans.runUid = ri.uid;
    ans.state = ri.state;
    ans.startAt = ri.startAt;
    ans.endAt = ri.endAt;
    ans.endResult = ri.endResult;
    try {
      ans.endTag = ri.transition.endTag;
    } catch (ex) {
    }
    return ans;
  }

}

export class Row {
  public runUid: string;
  public startAt: string;
  public endAt: string;
  public state: FlowState;
  public endTag = '';
  public endResult: Array<object>;
}
