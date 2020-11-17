import { JsonDataProvide } from './../../../uikit/json-editor/json-editor.component';
import { CommUtils } from './../../../utils/comm-utils';
import { Flow, BaseVar } from './../../flow';
import { Component, OnInit } from '@angular/core';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { plainToClass } from 'class-transformer';

declare var jquery: any;
declare let $: any;
declare let JSONEditor: any;

@Component({
  selector: 'app-flow',
  templateUrl: './flow.component.html',
  styleUrls: ['./flow.component.css']
})
export class FlowComponent implements OnInit, ClazzExComponent, JsonDataProvide {

  // public varJsonEditorId = CommUtils.makeAlphanumeric(16);
  data: any;
  constructor() { }
  getDataJson(): string {
    const o = plainToClass(Object, this.flow.vars);
    return JSON.stringify(o);
  }

  setDataJson(d: string): void {
    const o = JSON.parse(d);
    this.flow.vars = plainToClass(BaseVar, o);
  }

  init(d: any): void {
    this.data = d;
  }

  getData(): any {
    return this.flow;
  }


  public get flow(): Flow { return this.data; }


  async ngOnInit(): Promise<void> {
    await CommUtils.delay(10);
    // this.setupJsonEditor();
  }



  // private setupJsonEditor(): void {
  //   const container = document.getElementById(this.varJsonEditorId);
  //   const options = {
  //     onChangeJSON: (json) => {
  //       this.flow.vars = json;
  //     }
  //   };
  //   const editor = new JSONEditor(container, options);

  //   // set json
  //   const initialJson = this.flow.vars;
  //   editor.set(initialJson);
  // }

}
