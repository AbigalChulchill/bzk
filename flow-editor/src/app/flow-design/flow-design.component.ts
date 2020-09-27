import { ModifyingFlowService } from './../service/modifying-flow.service';
import { HttpClientService } from './../service/http-client.service';
import { Flow } from './../model/flow';
import { Component, OnInit } from '@angular/core';
import { CurPropertiesService } from './properties/cur-properties.service';

declare var jquery: any;
declare let $: any;

declare let mermaid: any;

@Component({
  selector: 'app-flow-design',
  templateUrl: './flow-design.component.html',
  styleUrls: ['./flow-design.component.css']
})
export class FlowDesignComponent implements OnInit {


  constructor(
    public modifyingFlow: ModifyingFlowService,
    private curProperties: CurPropertiesService
  ) { }

  async ngOnInit(): Promise<void> {
    if (!this.modifyingFlow.model) { await this.modifyingFlow.loadDemo(); }
    this.drawChart();


  }
  private drawChart(): void {
    const graphDefinition = $('#graphDiv').text();
    this.drawByCode(graphDefinition);
  }

  private drawByCode(code: string): void {
    const insertSvg = (svgCode, bindFunctions) => {
      $('#graphDiv').html(svgCode);
    };
    const graph = mermaid.mermaidAPI.render('graph', code, insertSvg);
  }

  public onFlowSettingClick(): void {
    this.curProperties.setTarget(this.modifyingFlow.model);
  }



}

