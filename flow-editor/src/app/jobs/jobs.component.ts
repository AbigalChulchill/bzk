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
  public flowPoolInfos = new Array<FlowPoolInfo>();
  public archiveRunInfosMap = new Map<string, Array<RunInfo>>()
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
    this.flowPoolInfos = await this.flowClient.listFlowPoolInfo().toPromise();
    this.reflesh(t);


  }

  private reflesh(t: number): void {
    const vdata = new Array<Row>();
    for (const sf of this.savedFlows) {
      vdata.push(this.genRow(sf));
      this.loadArchiveRunInfos(sf);
    }
    this.dataSource.data = vdata;
    this.loading.dismiss(t);

  }

  private async loadArchiveRunInfos(sf: Job): Promise<void> {
    let ris:Array<RunInfo> = await this.flowClient.listArchiveRunInfo(sf.uid).toPromise();
    let fpi = this.getPoolInfo(sf);
    for(const r of ris){
      fpi.runInfos.push(r);
    }
    this.reflesh(-1);
  }

  public getPoolInfo(sf: Job): FlowPoolInfo {
    return this.flowPoolInfos.find(fp => fp.flow.uid === sf.uid);
  }

  public getAllRunCount(sf: Job): number {
    const ri = this.flowPoolInfos.find(fp => fp.flow.uid === sf.uid);
    if (!ri) return 0;
    return ri.runInfos.length;
  }

  public getStateCount(sf: Job, st: FlowState): number {
    const ri = this.flowPoolInfos.find(fp => fp.flow.uid === sf.uid);
    if (!ri) return 0;
    return ri.runInfos.filter(r => r.state === st).length;
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
    const fr = this.getPoolInfo(sf);

    ans.enable = fr != null;
    if (!fr || fr.runInfos.length <= 0) return ans;
    const lri = fr.runInfos[fr.runInfos.length - 1];
    ans.lastState = lri.state;
    return ans;
  }


}

export class Row {
  public id: string;
  public name: string;
  public lastState: string;
  public lastTriggerAt: string;
  public enable: boolean;


}
