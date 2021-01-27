import { ObjTextProvide, TextProvide } from './../../infrastructure/meta';
import { Component, OnInit } from '@angular/core';
import { KVPair } from 'src/app/model/action';
import { ClazzExComponent, Prop, PropInfoArgs, PropType, PropUtils } from 'src/app/utils/prop-utils';
import { DialogService } from '../dialog.service';

@Component({
  selector: 'app-kvpair',
  templateUrl: './kvpair.component.html',
  styleUrls: ['./kvpair.component.css']
})
export class KVPairComponent implements OnInit, ClazzExComponent {
  public ObjTextProvide=ObjTextProvide;
  public data: KVPair;
  public valProp:Prop;

  constructor(
    public dialog:DialogService
  ) { }
  init(d: any, mataInfo: any): void {
    this.data = d;
    this.valProp = this.getValProp();
  }

  ngOnInit(): void {
  }

  public getKeyProvider(): TextProvide {
    return {
      getStr: () => this.data.key,
      setStr: s => this.data.key = s
    };
  }

  public getValProvider(): TextProvide {
    return {
      getStr: () => this.data.val,
      setStr: s => this.data.val = s
    };
  }

  private getValProp(): Prop {
    const ans= PropUtils.getInstance().genHasInfo('val', this.data,this.genPropInfo('val'));
    ans.info.hide = false;
    return ans;
  }

  private genPropInfo(t:string):PropInfoArgs{
    return {
      title: null,
      hide:true,
      type: PropType.MultipleText,
      // refInfo: {
      //   clazz: String,
      //   newObj: ''
      // }
    };
  }

}
