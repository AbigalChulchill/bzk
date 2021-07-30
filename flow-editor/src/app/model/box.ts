import { Transition } from './transition';
import { Type } from 'class-transformer';
import { BzkUtils, OTypeClass } from '../utils/bzk-utils';
import { PropClazz, PropEnums, PropInfo, PropType } from '../utils/prop-utils';
import { Action } from './action';
import { BzkObj } from './bzk-obj';
import { ModelUpdateAdapter } from './service/model-update-adapter';
import { Condition, ConditionNum } from './condition';
import { Constant } from '../infrastructure/constant';
import { CommUtils } from '../utils/comm-utils';
import { VarKey } from './var-key';
import { BaseVar } from '../infrastructure/meta';
import { LogLv } from './pojo/enums';

@PropClazz({
  title: 'Box',
  exView: 'BoxComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Box'
})
export class Box extends BzkObj {


  @PropInfo({
    title: 'name',
    type: PropType.Text,
    updatePredicate: (p, v) => ModelUpdateAdapter.getInstance().onRefleshCart()
  })
  public name = '';
  @Type(() => Action)
  public actions = new Array<Action>();
  @Type(() => Link)
  public links = new Array<Link>();
  @Type(() => BaseVar)
  @PropInfo({
    title: 'Box Var Declare',
    type: PropType.Custom,
    customView: 'JsonEditorComponent',
    customViewFolded: true
  })
  public vars = new BaseVar();
  public taskSort = Array<string>();
  @PropInfo({
    title: 'transition',
    type: PropType.Object,
    objectSamePage:true
  })
  @Type(() => Transition)
  public transition = new Transition();


  @PropInfo({
    title: 'whileJudgment',
    type: PropType.Custom,
    customView:'ConditionComponent',
    customViewFolded:true
  })
  @Type(() => Condition)
  public whileJudgment :Condition;

  @PropInfo({
    title: 'minLogLv',
    type: PropType.Enum
  })
  @PropEnums(Object.keys(LogLv))
  public minLogLv = LogLv.DEBUG;


  public getTask(uid: string): any {
    const fa = this.actions.find(a => a.uid === uid);
    if (fa) { return fa; }
    const fl = this.links.find(l => l.uid === uid);
    return fl;
  }

  public listTask(): Array<any> {
    const ans = new Array<any>();
    for (const uid of this.taskSort) {
      ans.push(this.getTask(uid));
    }
    return ans;
  }

  public removeTask(uid: string): void {
    const idx = this.actions.findIndex(a => a.uid === uid);
    const lidx = this.links.findIndex(a => a.uid === uid);
    if (idx >= 0) { this.actions.splice(idx, 1); }
    if (lidx >= 0) { this.links.splice(lidx, 1); }
    const uidx = this.taskSort.indexOf(uid);
    if (uidx >= 0) { this.taskSort.splice(uidx, 1); }
  }



  public getLastLink(): Link {
    const suid = this.taskSort[this.taskSort.length - 1];
    return this.getTask(suid);
  }

  public appendTaskUid(uid: string): void {
    this.taskSort.push(uid);
  }

  public appendAction(act: Action): void {
    this.actions.push(act);
    this.appendTaskUid(act.uid);
  }

  public appendLink(lk: Link): void {
    this.links.push(lk);
    this.appendTaskUid(lk.uid);
  }

}

@PropClazz({
  title: 'Link',
  exView: 'LinkComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Link'
})
export class Link extends BzkObj {
  @PropInfo({
    title: 'name',
    type: PropType.Text,
    updatePredicate: (p, v) => ModelUpdateAdapter.getInstance().onRefleshCart()
  })
  public name = '';

  @PropInfo({
    title: 'condition',
    type: PropType.Custom,
    customView:'ConditionComponent',
    customViewFolded:true
  })
  @Type(() => Condition)
  public condition: Condition;
  @PropInfo({
    title: 'transition',
    type: PropType.Object,
    objectSamePage:true
  })
  @Type(() => Transition)
  public transition = new Transition();

  public static gen(): Link {
    const l = new Link();
    l.uid = CommUtils.makeAlphanumeric(Constant.UID_SIZE);
    l.clazz = BzkUtils.getOTypeInfo(l.constructor).clazz;
    l.condition = ConditionNum.gen();
    return l;
  }
}





