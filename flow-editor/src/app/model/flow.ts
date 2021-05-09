import { StringUtils } from 'src/app/utils/string-utils';
import { CommUtils } from 'src/app/utils/comm-utils';
import { Box } from './box';
import { BzkObj } from './bzk-obj';
import { Entry } from './entry';
import { TimeUnit } from './enums';
import { PropClazz, PropInfo, PropType } from '../utils/prop-utils';
import { OTypeClass } from '../utils/bzk-utils';
import { Type } from 'class-transformer';
import { BaseVar } from '../infrastructure/meta';
import { ClassType } from 'class-transformer/ClassTransformer';

/*export class BzkObj {

  public clazz: string;
  public uid: string;

}*/
@OTypeClass({
  clazz: 'net.bzk.flow.model.Flow'
})
@PropClazz({
  title: 'Flow',
  exView: 'FlowComponent'
})
export class Flow extends BzkObj {


  @PropInfo({
    title: 'name',
    type: PropType.Text
  })
  public name: string;
  @PropInfo({
    title: 'logEncryptKey',
    type: PropType.Text
  })
  public logEncryptKey = '1234567890123456';
  @Type(() => Box)
  public boxs: Array<Box>;
  @Type(() => BaseVar)
  public vars = new BaseVar();

  public varCfgNames = new Array<string>();
  // @PropInfo({
  //   title: 'entry',
  //   type: PropType.Object
  // })
  // @Type(() => Entry)
  // public entry: Entry;
  @PropInfo({
    title: 'entrys',
    type: PropType.List,
    child: {
      type: PropType.Object,
      newObj: new Entry(),
    },
  })
  @Type(() => Entry)
  public entrys = new Array<Entry>();
  public threadCfg = new ThreadCfg();

  public getEntryBox(): Box {
    const evuid = this.entrys.filter(e => !StringUtils.isBlank(e.boxUid))[0].boxUid
    return this.boxs.find(b => evuid === b.uid);
  }

  public getBoxByChild(uid: string): Box {
    return this.boxs.find(b => b.taskSort.includes(uid));
  }

  public getEntry<T extends Entry>(cls: ClassType<T>): T {
    const el = this.entrys.filter(e => e.constructor == cls);
    if(el.length<=0) return null;
    return el[0] as T;
  }

}

export class ThreadCfg {
  public corePoolSize = 10;
  public maximumPoolSize = 50;
  public keepAliveTime = 500;
  public aliveUnit = TimeUnit.MINUTES;
}
