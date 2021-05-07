import { ConditionUtils } from './../../../../utils/condition-utils';
import { ConditionNum, NumCheckType } from './../../../condition';
import { Component, OnInit } from '@angular/core';
import { ClazzExComponent, Prop, PropInfoArgs, PropType, PropUtils } from 'src/app/utils/prop-utils';

@Component({
  selector: 'app-condition-num',
  templateUrl: './condition-num.component.html',
  styleUrls: ['./condition-num.component.css']
})
export class ConditionNumComponent implements OnInit, ClazzExComponent {


  data: any;
  public leftProp:Prop;
  public rightProp:Prop;
  constructor() { }

  public get condition(): ConditionNum { return this.data; }

  init(d: any, mi: any): void {
    this.data = d;
    this.leftProp = ConditionUtils.getProp('left',this.condition);
    this.rightProp = ConditionUtils.getProp('right',this.condition);
  }


  ngOnInit(): void {
  }

  public listJudgment(): Array<string> {
    return Object.keys(NumCheckType);
  }


}
