import { VarService } from './../../../../service/var.service';
import { PlaceholderUtils } from './../../../../utils/placeholder-utils';
import { VarUtils } from './../../../../utils/var-utils';
import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { RefSetType } from 'src/app/utils/prop-ref-val';
import { Prop } from 'src/app/utils/prop-utils';
import { map, startWith } from 'rxjs/operators';
import { ModelUpdateAdapter } from 'src/app/model/service/model-update-adapter';
import { ModifyingFlowService } from 'src/app/service/modifying-flow.service';
import { TextProvide } from 'src/app/infrastructure/meta';

@Component({
  selector: 'app-prop-ref',
  templateUrl: './prop-ref.component.html',
  styleUrls: ['./prop-ref.component.css']
})
export class PropRefComponent implements OnInit , TextProvide {
  RefSetType = RefSetType;
  @Input() prop: Prop;


  constructor(
    private varService:VarService
  ) { }


  ngOnInit(): void {
  }



  getStr(): string {
    const orgt = this.prop.refVal.refVal;
    return PlaceholderUtils.trimApachTag(orgt);
  }
  setStr(d: string): void {
    this.prop.refVal.refVal = PlaceholderUtils.genApachVar(d);
  }

  // public get refContent(): string {
  //   const orgt = this.prop.refVal.refVal;
  //   return PlaceholderUtils.trimApachTag(orgt);
  // }

  // public set refContent(s:string){
  //   this.prop.refVal.refVal = PlaceholderUtils.genApachVar(s);
  // }

}
