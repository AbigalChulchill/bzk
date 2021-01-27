import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { ConditionNum } from './../../../condition';
import { Component, Input, OnInit } from '@angular/core';
import { Condition, ConKind } from 'src/app/model/condition';

@Component({
  selector: 'app-condition-enumerator',
  templateUrl: './condition-enumerator.component.html',
  styleUrls: ['./condition-enumerator.component.css']
})
export class ConditionEnumeratorComponent implements OnInit  {

//   @Input() public conditionSG: ConditionSetGet;
//   private data:Condition;
//   public conditionSGs = new Array<ConditionSetGet>();

//   constructor() { }
//   init(d: any, mataInfo: any): void {
//     this.data = d;
//     this.conditionSG = {
//       getCondition:()=>this.data,
//       setCondition:s=> this.data = s
//     };
//     this.reflesh();
//   }

//   getCondition(): Condition {
//     return this.conditionSG.getCondition();
//   }
//   setCondition(c: Condition): void {
//     this.conditionSG.setCondition(c);
//     this.reflesh();
//   }

  ngOnInit(): void {
    // this.reflesh();
  }

//   reflesh(): void {
//     this.conditionSGs = this.listConditionSGs();
//   }

//   public listConKinds(): Array<string> {
//     return Object.keys(ConKind);
//   }

//   public onChangeConKind(csg: ConditionSetGet, ck: ConKind): void {
//     if (ck === ConKind.AND || ck === ConKind.OR) {
//       this.appendNewCon(csg);
//     } else {
//       csg.getCondition().next = null;
//     }
//     csg.getCondition().kind = ck;
//     this.reflesh();
//   }

//   private appendNewCon(csg: ConditionSetGet): void {
//     if (csg.getCondition().next) { return; }
//     csg.getCondition().next = ConditionNum.gen();
//   }

//   private listConditionSGs(): Array<ConditionSetGet> {
//     const ans = new Array<ConditionSetGet>();
//     for (let c: ConditionSetGet = this; c.getCondition(); c = this.nextCondition(c.getCondition())) {
//       ans.push(c);
//     }
//     return ans;
//   }

//   private nextCondition(c: Condition): ConditionSetGet {
//     return new ChildConditionSetGet(c, this);
//   }

// }

// export class ChildConditionSetGet implements ConditionSetGet {
//   private view: ConditionEnumeratorComponent;
//   public parents: Condition;

//   public constructor(p: Condition, v: ConditionEnumeratorComponent) {
//     this.view = v;
//     this.parents = p;
//   }

//   getCondition(): Condition {
//     return this.parents.next;
//   }
//   setCondition(c: Condition): void {
//     this.parents.next = c;
//     this.view.reflesh();
//   }

}
