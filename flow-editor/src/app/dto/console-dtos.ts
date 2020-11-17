import { BaseVar } from './../model/flow';
import { Uids } from '../model/uids';
import { VarLv } from '../model/pojo/enums';
import { plainToClass } from 'class-transformer';

export class ConsoleDtos {

  public static isRunLog(txt: string): boolean {
    if (ConsoleDtos.parseBoxRunLog(txt)) { return true; }
    if (ConsoleDtos.parseActionRunLog(txt)) { return true; }
    return false;
  }

  public static parseBoxRunLog(txt: string): BoxRunLog {
    const rex = /<B\%(.+)\%B>/g;
    const pa = rex.exec(txt);
    if (!pa || pa.length === 0) { return null; }
    if (pa.length > 2) { throw new Error(pa + ' is length > 1'); }
    const jTxt = pa[1];
    const pojo: BoxRunLog = JSON.parse(jTxt);
    pojo.orgText = txt;
    return plainToClass(BoxRunLog, pojo);
  }

  public static parseActionRunLog(txt: string): ActionRunLog {
    const rex = /\<A\%(.+)\%A\>/g;
    const pa = rex.exec(txt);
    if (!pa || pa.length === 0) { return null; }
    if (pa.length > 2) { throw new Error(pa + ' is length > 1'); }
    const jTxt = pa[1];
    const pojo: ActionRunLog = JSON.parse(jTxt);
    pojo.orgText = txt;
    return plainToClass(ActionRunLog, pojo);
  }


}

export class ReadingInfo {
  public keepReading = false;
}

export enum BoxRunState {
  BoxStart = 'BoxStart', EndFlow = 'EndFlow', LinkTo = 'LinkTo', StartAction = 'StartAction', EndAction = 'EndAction', ActionCall = 'ActionCall'

}

export class BoxRunLog {
  public orgText: string;
  public uids: Uids;
  public msg: string;
  public flowVar: BaseVar;
  public boxVar: BaseVar;
  public boxName: string;
  public state: BoxRunState;
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
