import { ClassType } from 'class-transformer/ClassTransformer';
import { CommUtils } from 'src/app/utils/comm-utils';
import { Uids } from '../model/uids';
import { VarLv } from '../model/pojo/enums';
import { plainToClass } from 'class-transformer';
import { BaseVar } from '../infrastructure/meta';

export class ConsoleDtos {

  public static isRunLog(dKey: string, txt: string): boolean {
    if (ConsoleDtos.parseBoxRunLog(dKey, txt)) { return true; }
    if (ConsoleDtos.parseActionRunLog(dKey, txt)) { return true; }
    return false;
  }

  public static parseBoxRunLog(dKey: string, txt: string): BoxRunLog {
    return this.parseRunLog<BoxRunLog>({
      clz: BoxRunLog,
      dKey,
      rex: /<B\%(.+)\%B>/g,
      txt
    });
  }

  public static parseActionRunLog(dKey: string, txt: string): ActionRunLog {
    return this.parseRunLog<ActionRunLog>({
      clz: ActionRunLog,
      dKey,
      rex: /\<A\%(.+)\%A\>/g,
      txt
    });
  }


  public static parseRunLog<T>(lb: LogB<T>): T {
    try {
      const pa = lb.rex.exec(lb.txt);
      if (!pa || pa.length === 0) { return null; }
      if (pa.length > 2) { throw new Error(pa + ' is length > 1'); }
      const jTxt = pa[1];
      const dj = CommUtils.decryptAES(lb.dKey, jTxt);
      const pojo = JSON.parse(dj);
      pojo.orgText = lb.txt;
      return plainToClass(lb.clz, pojo);
    } catch (ex) {
      console.error(ex);
      return null;
    }
  }


}

class LogB<T>{
  clz: ClassType<T>;
  rex: RegExp;
  txt: string;
  dKey: string;
}

export class ReadingInfo {
  public keepReading = false;
}

export enum BoxRunState {
  BoxStart = 'BoxStart', BoxLoop = 'BoxLoop', BoxLoopDone = 'BoxLoopDone', EndFlow = 'EndFlow', LinkTo = 'LinkTo',
  StartAction = 'StartAction', EndAction = 'EndAction', ActionCall = 'ActionCall',
  ActionCallFail = 'ActionCallFail', ActionCallWarn = 'ActionCallWarn', ActionResult = 'ActionResult', WhileLoopBottom = 'WhileLoopBottom'

}

export class BoxRunLog {
  public orgText: string;
  public uids: Uids;
  public msg: string;
  public flowVar: BaseVar;
  public boxVar: BaseVar;
  public boxName: string;
  public state: BoxRunState;
  public failed: boolean;
  public exception: any;
}

export class VarVal {
  public lv: VarLv;
  public key: string;
  public val: any;
}


export class ActionRunLog extends BoxRunLog {
  public varVals: Array<VarVal>;
  public actionName: string;
}
