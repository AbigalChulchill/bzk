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
    this.leftProp = this.getLeftProp();
    this.rightProp = this.getRightProp();
  }


  ngOnInit(): void {
  }

  public listJudgment(): Array<string> {
    return Object.keys(NumCheckType);
  }

  private getLeftProp(): Prop {
    const ans= PropUtils.getInstance().genHasInfo('left', this.condition,this.genPropInfo('left'));
    ans.info.hide = false;
    return ans;
  }

  private getRightProp(): Prop {
    const ans= PropUtils.getInstance().genHasInfo('right', this.condition,this.genPropInfo('right'));
    ans.info.hide = false;
    return ans;
  }

  private genPropInfo(t:string):PropInfoArgs{
    return {
      title: null,
      hide:true,
      type: PropType.Number,
      refInfo: {
        clazz: Number,
        newObj: 0
      }
    };
  }


}
