import { BaseVar } from "../infrastructure/meta";
import { RunState, VarLv } from "./pojo/enums";

export class RunLog {

  private id: number;

  private flowUid: string;
  private runFlowUid: string;
  private boxUid: string;
  private runBoxUid: string;
  private actionUid: string;
  private runActionUid: string;

  private msg: string;
  private flowVar: BaseVar;
  private boxVar: BaseVar;
  private boxName: string;
  private state = RunState.ActionCall;
  private failed = false;
  private exception: string;
  private exceptionClazz: string;
  private  varVals:Array<VarVal>;
  private actionName: string;

  private updateAt: string;
  private createAt: string;

}

export class VarVal {
  public lv: VarLv;
  public key: string;
  public val: any;
}
