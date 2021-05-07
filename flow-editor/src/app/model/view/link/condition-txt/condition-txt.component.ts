import { Prop, PropInfoArgs, PropType, PropUtils } from './../../../../utils/prop-utils';
import { Component, OnInit } from '@angular/core';
import { ConditionTxt, TxtCheckType } from 'src/app/model/condition';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { ConditionUtils } from 'src/app/utils/condition-utils';

@Component({
  selector: 'app-condition-txt',
  templateUrl: './condition-txt.component.html',
  styleUrls: ['./condition-txt.component.css']
})
export class ConditionTxtComponent implements OnInit, ClazzExComponent {
  public leftProp:Prop;
  public rightProp:Prop;
  data: any;

  constructor() { }

  public get condition(): ConditionTxt { return this.data; }

  init(d: any, mi: any): void {
    this.data = d;

    this.leftProp = ConditionUtils.getProp('left',this.condition);
    this.rightProp = ConditionUtils.getProp('right',this.condition);
  }


  ngOnInit(): void {
  }

  public listJudgment(): Array<string> {
    return Object.keys(TxtCheckType);
  }


}
