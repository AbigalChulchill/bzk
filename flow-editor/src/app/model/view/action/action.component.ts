import { HttpClientService } from './../../../service/http-client.service';
import { ActionDebugData } from './../../../dto/debug-dtos';
import { ModifyingFlowService } from './../../../service/modifying-flow.service';
import { ToastService } from './../../../service/toast.service';
import { ModelUpdateAdapter } from './../../service/model-update-adapter';
import { Action } from './../../action';
import { Component, OnInit } from '@angular/core';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { PropertiesComponent } from 'src/app/flow-design/properties/properties.component';



@Component({
  selector: 'app-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.css']
})
export class ActionComponent implements OnInit, ClazzExComponent {

  data: any;

  flowVarKey: string;
  boxVarKey: string;

  constructor(
    private modifyingFlow: ModifyingFlowService,
    private httpClient: HttpClientService,
    private toast: ToastService
  ) { }

  init(d: any): void {
    this.data = d;
  }

  getData(): any {
    return this.action;
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
    rb.boxVar = this.modifyingFlow.varsStore.getVars(this.boxVarKey);
    rb.flowVar = this.modifyingFlow.varsStore.getVars(this.flowVarKey);
    rb.flow = ModelUpdateAdapter.getInstance().getFlow();
    rb.uid = this.action.uid;
    await this.httpClient.debugAction(rb, -1).toPromise();
  }

  public listVarKeys(): Array<string> {
    return this.modifyingFlow.varsStore.listKeys();
  }

  public onInternalVars(): void {
    PropertiesComponent.getInstance().getCurProperties().nextTarget({
      key: '',
      obj: this.modifyingFlow.varsStore
    });
  }


}
