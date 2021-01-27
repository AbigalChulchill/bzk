import { VarLv } from './../model/pojo/enums';
import { CommUtils } from './comm-utils';
import { StringUtils } from './string-utils';
import { VarLvF } from '../model/pojo/enums';
import { Prop } from './prop-utils';
import { PlaceholderUtils } from './placeholder-utils';
import { Constant } from '../infrastructure/constant';
export class PropRefVal {

  private prop: Prop;
  private valObj: any;
  // private valPlain: string;

  public constructor(p: Prop, orgT: string) {
    this.prop = p;
    this.init(orgT);
  }

  private init(iv: string): void {
    if (VarLvF.isVarLvExp(iv)) {
      this.prop.setOrgVal(iv);
      return;
    }
    if (StringUtils.isBlank(iv)) {
      this.resetVal();
      return;
    }
    if(this.prop.info.refInfo.clazz){
      this.valObj = CommUtils.convertByJson(this.prop.info.refInfo.clazz, iv);
    }else{
      this.valObj = JSON.parse(iv);
    }
    this.prop.setOrgVal(iv);
  }

  private resetVal(): void {
    this.val = (CommUtils.clone(this.prop.info.refInfo.newObj));
  }


  public set val(inv: any) {
    this.valObj = inv;
    this.prop.setOrgVal(JSON.stringify(inv));
  }

  public get val(): any {
    return this.valObj;
  }

  public set refVal(rs: string) {
    this.prop.setOrgVal(rs);
  }

  public get refVal(): string {
    return this.prop.getOrgVal();
  }


  public getType(): RefSetType {
    return PlaceholderUtils.isApachVar(this.prop.getOrgVal()) ? RefSetType.ByRef : RefSetType.ByVal;
  }

  public isType(rst:RefSetType):boolean{
    return this.getType() === rst;
  }

  public setType(rt: RefSetType): void {
    if (rt === RefSetType.ByRef) {
      this.refVal = PlaceholderUtils.genApachVar(Constant.TODO_VAR_KEY);
    } else {
      this.resetVal();
    }
  }


}

export enum RefSetType {
  ByRef, ByVal
}
