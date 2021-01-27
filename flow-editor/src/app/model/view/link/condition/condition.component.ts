import { ConditionNum } from './../../../condition';
import { CommUtils } from './../../../../utils/comm-utils';
import { async } from '@angular/core/testing';
import { Component, Host, Input, OnInit, ViewChild } from '@angular/core';
import { PropertiesComponent } from 'src/app/flow-design/properties/properties.component';
import { Condition, ConKind } from 'src/app/model/condition';
import { BzkUtils } from 'src/app/utils/bzk-utils';
import { ClazzExComponent, Prop } from 'src/app/utils/prop-utils';

@Component({
  selector: 'app-condition',
  templateUrl: './condition.component.html',
  styleUrls: ['./condition.component.css']
})
export class ConditionComponent implements OnInit, ClazzExComponent {

  @Input() public set dataInit(c: Condition) { this.init(c, null); }
  public data: Condition;
  public prop: Prop;

  constructor() { }


  init(d: any, mataInfo: any): void {
    this.data = d;
    this.prop = mataInfo;
  }

  ngOnInit(): void {
  }



  public listCoditionClz(): Array<string> {
    return BzkUtils.listOtypeKeys(Condition);
  }

  public onChangeClazz(clz: string): void {
    const type = BzkUtils.getTypeByClazz(clz);
    const ti = type.gen();
    this.data = ti;
    this.prop.setOrgVal(this.data);
    console.log(JSON.stringify(this.data));
  }


  public listConKinds(): Array<string> {
    return Object.keys(ConKind);
  }


  public onChangeConKind(ck: ConKind): void {
    if (ck === ConKind.AND || ck === ConKind.OR) {
      this.data.next = ConditionNum.gen();
    } else {
      this.data.next = null;
    }

  }

  public get next(): Condition { return this.data.next; }

  public set next(c: Condition) { this.data.next = c; }

}





