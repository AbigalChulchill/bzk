import { JobClientService } from './../service/job-client.service';
import { Flow } from 'src/app/model/flow';
import { GithubService } from 'src/app/service/github.service';
import { async } from '@angular/core/testing';
import { DialogService } from './../uikit/dialog.service';
import { FlowClientService } from './../service/flow-client.service';
import { LoadingService } from './../service/loading.service';
import { FlowPoolInfo, FlowState, RunInfo } from './../dto/flow-pool-info';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { HttpClientService } from '../service/http-client.service';
import { LoadSource, ModifyingFlowService } from '../service/modifying-flow.service';
import { Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Job } from '../model/job';
import { JobRunInfo } from '../dto/job-dto';
import { plainToClass } from 'class-transformer';



@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrls: ['./jobs.component.css']
})
export class JobsComponent implements OnInit, AfterViewInit {


  displayedColumns: string[] = ['enable', 'id', 'name', 'lastState', 'lastTriggerAt', 'triggerInfo', 'actions'];
  dataSource: MatTableDataSource<Row>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  public FlowState = FlowState;
  public archiveRunInfosMap = new Map<string, JobRunInfo>()
  public savedFlows: Array<Job>;
  // panelOpenState = false;
  constructor(
    private loading: LoadingService,
    private flowClient: FlowClientService,
    private jobClient: JobClientService,
    private modifyingFlow: ModifyingFlowService,
    private githubService: GithubService,
    private dialog: DialogService
  ) {
    this.dataSource = new MatTableDataSource(new Array<Row>());
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  async ngOnInit(): Promise<void> {
    await this.reload();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  private async reload(): Promise<void> {
    const t = this.loading.show();
    this.savedFlows = await this.jobClient.listAll();
    for (const sf of this.savedFlows) {
      this.loadArchiveRunInfos(sf);
    }
    this.reflesh(t);


  }

  private reflesh(t: number): void {
    const vdata = new Array<Row>();
    for (const sf of this.savedFlows) {
      vdata.push(this.genRow(sf));
    }
    this.dataSource.data = vdata;
    this.loading.dismiss(t);

  }

  private async loadArchiveRunInfos(sf: Job): Promise<void> {
    let ris: JobRunInfo = await this.jobClient.getInfo(sf.uid);
    this.archiveRunInfosMap.set(sf.uid,ris);
    this.reflesh(-1);
  }

  private getJobRunInfo(sf: Job):JobRunInfo{
    if( this.archiveRunInfosMap.has(sf.uid)){
      return this.archiveRunInfosMap.get(sf.uid);
    }
    return new JobRunInfo();
  }

  public getAllRunCount(sf: Job): number {
    return this.getJobRunInfo(sf).allCount;
  }

  public getStateCount(sf: Job, st: FlowState): number {
    return this.getJobRunInfo(sf).getStateCount(st);
  }

  public async forceRemovePool(uid: string): Promise<void> {
    await this.flowClient.forceRemovePool(uid).toPromise();
    await this.reload();
  }

  public openImportGistDialog(): void {
    this.dialog.openCloudBackup(async () => {
      await this.reload();
    });
  }

  public async uploadAllGistDialog(): Promise<void> {
    const t = this.loading.show();
    const ml = new Array<Flow>();
    for (const sf of this.savedFlows) {
      ml.push(sf.model);
    }
    await this.githubService.save(ml);
    this.loading.dismiss(t);
  }

  public async onFileEdit(grid: string): Promise<void> {
    const gr = this.getSavedFlow(grid);
    this.modifyingFlow.goDesignPage(gr.model);
  }

  public getSavedFlow(grid: string): Job { return this.savedFlows.find(f => f.uid === grid); }

  private genRow(sf: Job): Row {
    const ans = new Row();
    ans.id = sf.uid;
    ans.name = sf.model.name;
    ans.lastTriggerAt = this.getJobRunInfo(sf).lastStartAt;
    ans.enable = this.getJobRunInfo(sf).enable;
    ans.lastState = this.getJobRunInfo(sf).lastState;
    return ans;
  }


}

export class Row {
  public id: string;
  public name: string;
  public lastState: string;
  public lastTriggerAt: Date;
  public enable: boolean;


}
