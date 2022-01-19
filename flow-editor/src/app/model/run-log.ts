import { BaseVar } from "../infrastructure/meta";
import { LogLv, RunState, VarLv } from "./pojo/enums";

export class RunLog {

  public id: number;

  public flowUid: string;
  public runFlowUid: string;
  public boxUid: string;
  public runBoxUid: string;
  public actionUid: string;
  public runActionUid: string;
  public refRunFlowUid: string;
  public refFlowUid: string;

  public msg: string;
  public flowVar: BaseVar;
  public boxVar: BaseVar;
  public boxName: string;
  public state = RunState.ActionCall;
  public logLv = LogLv.DEBUG;
  public exception: string;
  public exceptionClazz: string;
  public varVals: Array<VarVal>;
  public actionName: string;

  public updateAt: string;
  public createAt: string;

}

export class VarVal {
  public lv: VarLv;
  public key: string;
  public val: any;
}
