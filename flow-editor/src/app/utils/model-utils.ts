import { Flow } from './../model/flow';
import { Action, NodejsAction } from '../model/action';
import { Box, Link } from '../model/box';
import { BzkUtils } from './bzk-utils';
import { CommUtils } from './comm-utils';
import { Constant } from '../infrastructure/constant';

export class ModelUtils {

  public static createSimpleBox(f: Flow): Box {
    const ans = new Box();
    ans.clazz = BzkUtils.getOTypeInfo(Box).clazz;
    ans.uid = CommUtils.makeAlphanumeric(Constant.UID_SIZE);
    const act = NodejsAction.gen();
    ans.actions.push(act);
    const lk = Link.gen();
    lk.endTag = lk.uid + ' End';
    ans.links.push(lk);
    ans.taskSort.push(act.uid);
    ans.taskSort.push(lk.uid);
    f.boxs.push(ans);
    return ans;
  }

  public static createAction(b: Box): void {
    const na = NodejsAction.gen();
    b.appendAction(na);
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
      if (l.toBox === boxUid) { l.toBox = ''; }
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

}
