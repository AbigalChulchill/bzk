import { VarCfgService } from './../../../service/var-cfg.service';
import { DialogService } from './../../../uikit/dialog.service';
import { ListLogType, RunLogClientService } from './../../../service/run-log-client.service';
import { FlowClientService } from './../../../service/flow-client.service';
import { FlowDesignComponent } from './../../../flow-design/flow-design.component';
import { HttpClientService } from './../../../service/http-client.service';
import { ActionDebugData } from './../../../dto/debug-dtos';
import { ModifyingFlowService } from './../../../service/modifying-flow.service';
import { ToastService } from './../../../service/toast.service';
import { ModelUpdateAdapter } from './../../service/model-update-adapter';
import { Action } from './../../action';
import { Component, Input, OnInit } from '@angular/core';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { PropertiesComponent } from 'src/app/flow-design/properties/properties.component';
import { BaseVar } from 'src/app/infrastructure/meta';



@Component({
  selector: 'app-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.css']
})
export class ActionComponent implements OnInit, ClazzExComponent {

  @Input() protected data: any;

  flowVarKey: string;
  boxVarKey: string;

  constructor(
    private dialogService: DialogService,
    private flowClient: FlowClientService,
    private toast: ToastService,
    private varCfg: VarCfgService
  ) { }

  init(d: any, mi: any): void {
    this.data = d;
  }

  public get action(): Action { return this.data; }

  ngOnInit(): void {
  }

  public async testMe(): Promise<void> {
    this.toast.twinkle({
      msg: '已送出',
      title: ''
    });
    const rb = new ActionDebugData();
    rb.boxVar = this.varCfg.get(this.boxVarKey).content;
    if (!rb.boxVar) rb.boxVar = new BaseVar();
    rb.flowVar = this.varCfg.get(this.flowVarKey).content;
    if (!rb.flowVar) rb.flowVar = new BaseVar();
    rb.flow = ModelUpdateAdapter.getInstance().getFlow();
    rb.uid = this.action.uid;
    await this.flowClient.debugAction(rb, -1).toPromise();
  }

  public listVarKeys(): Array<string> {
    return this.varCfg.list.map(v => v.name);
  }

  public onInternalVars(): void {
    FlowDesignComponent.getInstance().propertiesView.getCurProperties().nextTarget({
      key: '',
      obj: null //TODO
    });
  }

  public onOpenLogDialog(): void {
    this.dialogService.openRunLoag(this.action.uid, ListLogType.action);
  }


}
