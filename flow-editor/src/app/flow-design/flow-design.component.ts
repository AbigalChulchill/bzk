import { SVGZoomService } from './svgzoom.service';
import { FlowDesignMenuService } from './flow-design-menu.service';
import { ModelUtils } from './../utils/model-utils';
import { CommUtils } from './../utils/comm-utils';
import { Action, NodejsAction } from './../model/action';
import { LinkCB, ModelUpdate, ModelUpdateAdapter } from './../model/service/model-update-adapter';
import { BzkUtils } from './../utils/bzk-utils';
import { ModelChart } from './../infrastructure/model-chart';
import { ChartClickInfo, ChartUtils, Node, Subgraph } from './../utils/chart-utils';
import { ModifyingFlowService } from './../service/modifying-flow.service';
import { HttpClientService } from './../service/http-client.service';
import { Flow } from './../model/flow';
import { Component, OnInit, ViewChild } from '@angular/core';
import { GithubService } from '../service/github.service';
import { Router } from '@angular/router';
import { PropertiesComponent } from './properties/properties.component';
import { OType } from '../model/bzk-obj';
import { Box } from '../model/box';

declare var jquery: any;
declare let $: any;

export enum FlowDesignClickSort {
  Default
}

@Component({
  selector: 'app-flow-design',
  templateUrl: './flow-design.component.html',
  styleUrls: ['./flow-design.component.css']
})
export class FlowDesignComponent implements OnInit, ModelUpdate {

  private static instance: FlowDesignComponent;
  @ViewChild(PropertiesComponent) public propertiesView: PropertiesComponent;

  private modelChart: ModelChart = new ModelChart('#graphDiv');
  private linkCB: LinkCB;


  constructor(
    public modifyingFlow: ModifyingFlowService,
    public githubService: GithubService,
    private router: Router,
    public menu: FlowDesignMenuService,
    public svgZoom:SVGZoomService
  ) {
    this.modelChart.getClicks().set(FlowDesignClickSort.Default, ci => this.onObjClick(ci));
  }

  public static getInstance(): FlowDesignComponent { return FlowDesignComponent.instance; }

  onLinkBox(cb: LinkCB): void {
    this.linkCB = cb;
  }

  getFlow(): Flow {
    return this.modifyingFlow.modelobs.getModel();
  }

  onClazzUpdate(newObj: OType, oldObj: OType): void {
    CommUtils.replaceByVal(this.modifyingFlow.modelobs.getModel(), oldObj, newObj);
    this.propertiesView.getCurProperties().replaceTarget(oldObj, newObj);
    // throw new Error('Method not implemented.');
  }

  onRefleshCart(): void {
    this.svgZoom.fitCurWidth(()=> this.modelChart.refleshBuilder());
  }

  async ngOnInit(): Promise<void> {
    FlowDesignComponent.instance = this;
    if (!this.githubService.hasAuth()) {
      this.githubService.postAuth(this.router.url);
      return;
    }
    await this.modifyingFlow.loadInit();
    this.modifyingFlow.modelobs.addObservable(this.modelChart);
    ModelUpdateAdapter.getInstance().setCurUpdater(this);
    try {
      this.svgZoom.setTar('#graphDiv svg','#graphDiv');
    } catch (error) {
      console.warn(error);
    }
  }

  private onObjClick(ci: ChartClickInfo): boolean {
    const bo = BzkUtils.findByUid(this.modifyingFlow.modelobs.getModel(), ci.srcId);
    if (this.linkCB) {
      if (bo instanceof Box) { this.linkCB(bo); }
      return false;
    }
    this.propertiesView.getCurProperties().setTarget({
      key: '',
      obj: bo
    });
    return false;
  }


  public onFlowSettingClick(): void {
    this.propertiesView.getCurProperties().setTarget({
      obj: this.modifyingFlow.modelobs.getModel(),
      key: '<root>'
    });
  }

  public onInternalVars(): void {
    this.propertiesView.getCurProperties().setTarget({
      key: '',
      obj: this.modifyingFlow.varsStore
    });
  }



}

