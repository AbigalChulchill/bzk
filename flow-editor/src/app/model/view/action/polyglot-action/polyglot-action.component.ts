import { VarLv, VarLvF } from './../../../pojo/enums';
import { TextProvide } from './../../../../infrastructure/meta';
import { Component, OnInit } from '@angular/core';
import { PolyglotAction } from 'src/app/model/action';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { ActionComponent } from '../action.component';

@Component({
  selector: 'app-polyglot-action',
  templateUrl: './polyglot-action.component.html',
  styleUrls: ['./polyglot-action.component.css']
})
export class PolyglotActionComponent extends ActionComponent implements TextProvide {


  public get polyglotAction(): PolyglotAction { return this.data; }

  ngOnInit(): void {
  }

  getStr(): string {
    const pf = VarLvF.getPrefix(this.polyglotAction.resultLv);
    return pf + this.polyglotAction.resultKey;
  }

  setStr(d: string): void {
    const pv = VarLvF.parse(d);
    if (pv) {
      this.polyglotAction.resultKey = pv.key;
      this.polyglotAction.resultLv = pv.lv;
    } else {
      this.polyglotAction.resultKey = d;
      this.polyglotAction.resultLv = VarLv.not_specify;
    }

  }

}
