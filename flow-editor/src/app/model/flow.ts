import { Condition } from './condition';
import { Link } from './link';
import { BzkObj } from './bzk-obj';
import { Action } from './action';
import { Entry } from './entry';
import { TimeUnit } from './enums';
import { PropInfo, PropType } from '../utils/prop-utils';

/*export class BzkObj {

  public clazz: string;
  public uid: string;

}*/

export class Flow extends BzkObj {

  @PropInfo({
    title: 'name',
    titleI18nKey: 'name',
    type: PropType.Text
  })
  public name: string;
  public boxs: Array<Box>;
  public vars = new BaseVar();
  public entry: Entry;
  public threadCfg = new ThreadCfg();

}

export class ThreadCfg {
  public corePoolSize = 10;
  public maximumPoolSize = 50;
  public keepAliveTime = 500;
  public aliveUnit = TimeUnit.MINUTES;
}

export class Box extends BzkObj {
  public actions: Array<Action>;
  public conditions: Array<Condition>;
  public links: Array<Link>;
  public vars: BaseVar;
  public taskSort: Array<string>;
}





export class BaseVar extends Map<string, object>{

}
