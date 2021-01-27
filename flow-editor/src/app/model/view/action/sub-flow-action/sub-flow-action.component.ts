import { VarKey } from './../../../var-key';
import { FlowLoadService, FlowRaw } from './../../../../service/flow-load.service';
import { Flow } from './../../../flow';
import { AfterViewInit, Component, Inject, OnInit, ViewChild } from '@angular/core';
import { KVPair, SubFlowAction } from 'src/app/model/action';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { GithubService } from 'src/app/service/github.service';
import { Gist } from 'src/app/dto/gist';
import { ModifyingFlowService } from 'src/app/service/modifying-flow.service';
import { Router } from '@angular/router';
import { StringUtils } from 'src/app/utils/string-utils';
import { PluginEntry } from 'src/app/model/entry';
import { PropertiesComponent } from 'src/app/flow-design/properties/properties.component';
import { VarKeyReflect } from 'src/app/infrastructure/meta';





@Component({
  selector: 'app-sub-flow-action',
  templateUrl: './sub-flow-action.component.html',
  styleUrls: ['./sub-flow-action.component.css']
})
export class SubFlowActionComponent implements AfterViewInit, OnInit, ClazzExComponent {

  displayedColumns: string[] = ['id', 'name', 'action'];
  dataSource: MatTableDataSource<FlowRaw>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  public data: SubFlowAction;

  constructor(
    public githubService: GithubService,
    private flowLoad: FlowLoadService,
    private router: Router,
    @Inject(PropertiesComponent) private parentView: PropertiesComponent
  ) {
  }
  async ngOnInit(): Promise<void> {

  }


  init(d: any, mataInfo: any): void {
    this.data = d;
    this.onInit();
  }

  public async onInit(): Promise<void> {
    if (StringUtils.isBlank(this.data.flowUid)) return;
    if (this.data.inputData.length > 0) return;
    const f = await FlowLoadService.getInstance().find(this.data.flowUid);
    if (!f) return;
    if (!(f.getModel().entry instanceof PluginEntry)) return;
    const pe: PluginEntry = f.getModel().entry as PluginEntry;
    for (const k of pe.requiredKeys) {
      const kp = new KVPair();
      kp.key = k;
      this.data.inputData.push(kp);
    }
  }




  async ngAfterViewInit(): Promise<void> {

    // const users = Array.from({ length: 100 }, (_, k) => createNewUser(k + 1));
    // Assign the data to the data source for the table to render
    this.dataSource = new MatTableDataSource(await this.flowLoad.getRaws());
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  public isSelected(r: FlowRaw): boolean {
    return this.data.flowUid === r.id;
  }

  public selectModel(r: FlowRaw): void {
    this.data.flowUid = r.id;
    this.data.inputData = this.genInitinputDataKV(r);
    this.data.outputReflects = this.genInitOutputReflects(r);
    this.parentView.curProperties.reflesh();
  }

  private genInitinputDataKV(r: FlowRaw): Array<KVPair> {
    const ans = new Array<KVPair>();
    const ets: PluginEntry = r.getModel().entry as PluginEntry;
    for(const rk of ets.requiredKeys){
      ans.push({
        key:rk,
        val:'TODO'
      });
    }
    return ans;
  }

  private genInitOutputReflects(r: FlowRaw): Array<VarKeyReflect> {
    const ans = new Array<VarKeyReflect>();
    const ets: PluginEntry = r.getModel().entry as PluginEntry;
    for(const rk of ets.outputKeys){
      const vkr = new VarKeyReflect();
      vkr.srcKey = rk;
      ans.push(vkr);
    }
    return ans;
  }


  public unselectModel(): void {
    this.data.flowUid = null;
  }

  public onFlowEdit(r: FlowRaw): void {
    this.router.navigate(['model/repo']);
    // this.modifyingFlow.setTarget(r.getModel(), null);
  }

}

