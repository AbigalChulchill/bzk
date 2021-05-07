import { Component, OnInit } from '@angular/core';
import { ConditionTime, TimeCheckType, TxtCheckType } from 'src/app/model/condition';
import { ConditionUtils } from 'src/app/utils/condition-utils';
import { ClazzExComponent, Prop } from 'src/app/utils/prop-utils';

@Component({
  selector: 'app-condition-time',
  templateUrl: './condition-time.component.html',
  styleUrls: ['./condition-time.component.css']
})
export class ConditionTimeComponent implements OnInit,ClazzExComponent {

  public data:ConditionTime = null;
  public leftProp:Prop;
  public rightProp:Prop;

  constructor() { }


  init(d: any, mataInfo: any): void {
    this.data = d;
    this.leftProp = ConditionUtils.getProp('left',this.data);
    this.rightProp = ConditionUtils.getProp('right',this.data);
  }

  ngOnInit(): void {
  }


  public listJudgment(): Array<string> {
    return Object.keys(TimeCheckType);
  }

}
