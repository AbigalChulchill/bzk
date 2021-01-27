import { Prop, PropInfoArgs, PropType, PropUtils } from './../../../../utils/prop-utils';
import { Component, OnInit } from '@angular/core';
import { ConditionTxt, TxtCheckType } from 'src/app/model/condition';
import { ClazzExComponent } from 'src/app/utils/prop-utils';

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
    this.leftProp = this.getLeftProp();
    this.rightProp = this.getRightProp();
  }


  ngOnInit(): void {
  }

  public listJudgment(): Array<string> {
    return Object.keys(TxtCheckType);
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
      title: t,
      hide:true,
      type: PropType.Text,
      refInfo: {
        clazz: String,
        newObj: ''
      }
    };
  }



}
