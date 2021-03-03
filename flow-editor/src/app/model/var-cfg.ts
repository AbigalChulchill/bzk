import { BaseVar } from './../infrastructure/meta';
export class VarCfg {

  public uid:string;
  public name: string;
  public description: string;
  public sha256: string;
  public updateAt:Date;
  public createAt:Date;

  public content : BaseVar;

}
