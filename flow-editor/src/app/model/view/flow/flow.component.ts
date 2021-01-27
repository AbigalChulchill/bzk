import { OType } from './../../bzk-obj';
import { BzkUtils } from './../../../utils/bzk-utils';
import { CommUtils } from './../../../utils/comm-utils';
import { Flow } from './../../flow';
import { Component, OnInit } from '@angular/core';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { plainToClass } from 'class-transformer';
import { BaseVar, TextProvide } from 'src/app/infrastructure/meta';
import { Entry } from '../../entry';

declare var jquery: any;
declare let $: any;
declare let JSONEditor: any;

@Component({
  selector: 'app-flow',
  templateUrl: './flow.component.html',
  styleUrls: ['./flow.component.css']
})
export class FlowComponent implements OnInit, ClazzExComponent, TextProvide {

  data: any;
  public entryTypes = new Array<OType>();
  constructor() { }
  getStr(): string {
    const o = plainToClass(Object, this.flow.vars);
    return JSON.stringify(o);
  }

  setStr(d: string): void {
    const o = JSON.parse(d);
    this.flow.vars = plainToClass(BaseVar, o);
  }

  init(d: any, mi: any): void {
    this.data = d;
  }



  public get flow(): Flow { return this.data; }


  async ngOnInit(): Promise<void> {
    this.listEntryType();
  }

  public listEntryType(): void {
    const ecs = BzkUtils.listChilds(Entry);
    for (const ec of ecs) {
      const ot = BzkUtils.getOTypeInfo(ec);
      this.entryTypes.push(ot);
    }

  }

  public onEntryTypeChange(e): void {
    console.log(e);
    const ot = BzkUtils.getTypeByClazz(e);
    const ne = new ot();
    const orgEbUid = this.flow.entry.boxUid;
    this.flow.entry = ne;
    this.flow.entry.boxUid = orgEbUid;
    this.flow.entry.clazz = BzkUtils.getOTypeInfo(ot).clazz;

  }


}
