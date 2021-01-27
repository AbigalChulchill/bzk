import { VarLvF } from './../model/pojo/enums';
import { ClassType } from 'class-transformer/ClassTransformer';
import { Flow,  } from './../model/flow';
import { Action, NodejsAction } from '../model/action';
import { Box, Link } from '../model/box';
import { BzkUtils } from './bzk-utils';
import { CommUtils } from './comm-utils';
import { Constant } from '../infrastructure/constant';
import { Condition, ConditionNum } from '../model/condition';
import { retry } from 'rxjs/operators';
import { BzkObj } from '../model/bzk-obj';

export class ModelUtils {


  public static pushUnique(ls:Array<BzkObj>, o:BzkObj):boolean{
    const eo = ls.filter(e=> e.uid === o.uid);
    if(eo.length>0) return false;
    ls.push(o);
    return true;
  }

  public static createSimpleBox(f: Flow): Box {
    const ans = new Box();
    ans.clazz = BzkUtils.getOTypeInfo(Box).clazz;
    ans.uid = CommUtils.makeAlphanumeric(Constant.UID_SIZE);
    const act = NodejsAction.gen();
    ans.actions.push(act);
    ans.taskSort.push(act.uid);

    f.boxs.push(ans);
    return ans;
  }

  public static createAction(actionClazz: string, b: Box): void {
    const clz = BzkUtils.getTypeByClazz(actionClazz);
    const na = new clz();
    na.uid = CommUtils.makeAlphanumeric(Constant.UID_SIZE);
    na.clazz = BzkUtils.getOTypeInfo(na.constructor).clazz;

    b.appendAction(na);
  }

  public static createCondition(): Condition {
    const ans = new ConditionNum();
    return ans;
  }

  public static createLink(b: Box): void {
    const l = Link.gen();
    b.appendLink(l);
  }

  public static removeBox(f: Flow, boxUid: string): void {
    const bidx = f.boxs.findIndex(b => b.uid === boxUid);
    f.boxs.splice(bidx, 1);
    const lks = this.listAllLinks(f);
    lks.forEach(l => {
      if (l.transition.toBox === boxUid) { l.transition.toBox = ''; }
      //TODO BOX
    });
  }

  public static listAllLinks(f: Flow): Array<Link> {
    const ans = new Array<Link>();
    const bs = f.boxs.sort(b => f.entry.boxUid === b.uid ? 1 : -1);
    for (const b of bs) {
      for (const l of b.links) {
        ans.push(l);
      }
    }
    return ans;
  }



  public static listAllAction(f: Flow): Array<Action> {
    const ans = new Array<Action>();
    for (const b of f.boxs) {
      for (const l of b.actions) {
        ans.push(l);
      }
    }
    return ans;
  }





  public static shiftTaskToBefore(b: Box, tUid: string): void {
    if (b.taskSort.length <= 1) { throw new Error('the length shound >=2'); }
    if (b.taskSort[0] === tUid) { throw new Error('can not shift first'); }
    const oidx = b.taskSort.findIndex(e => e === tUid);
    b.taskSort.splice(oidx, 1);
    b.taskSort.splice(oidx - 1, 0, tUid);
  }

  public static shiftTaskToNext(b: Box, tUid: string): void {
    if (b.taskSort.length <= 1) { throw new Error('the length shound >=2'); }
    if (b.taskSort[b.taskSort.length - 1] === tUid) { throw new Error('can not shift last'); }
    const oidx = b.taskSort.findIndex(e => e === tUid);
    b.taskSort.splice(oidx, 1);
    b.taskSort.splice(oidx + 1, 0, tUid);
  }



}
