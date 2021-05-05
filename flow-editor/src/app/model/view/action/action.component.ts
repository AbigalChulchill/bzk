import { VarCfgService } from './../../../service/var-cfg.service';
import { DialogService } from './../../../uikit/dialog.service';
import { ListLogType, RunLogClientService } from './../../../service/run-log-client.service';
import { FlowClientService } from './../../../service/flow-client.service';
import { FlowDesignComponent } from './../../../flow-design/flow-design.component';
import { HttpClientService } from './../../../service/http-client.service';
import { ModifyingFlowService } from './../../../service/modifying-flow.service';
import { ToastService } from './../../../service/toast.service';
import { ModelUpdateAdapter } from './../../service/model-update-adapter';
import { Action } from './../../action';
import { Component, Input, OnInit } from '@angular/core';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { PropertiesComponent } from 'src/app/flow-design/properties/properties.component';
import { BaseVar } from 'src/app/infrastructure/meta';
import { JobClientService } from 'src/app/service/job-client.service';



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
    private varCfg: VarCfgService,
    private jobClient: JobClientService,
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
    await this.jobClient.save(ModelUpdateAdapter.getInstance().getFlow()).toPromise();
    await this.flowClient.debugAction(ModelUpdateAdapter.getInstance().getFlow().uid,this.action.uid, -1).toPromise();
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
