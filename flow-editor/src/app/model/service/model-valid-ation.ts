import { Flow } from 'src/app/model/flow';
import { ModelUtils } from 'src/app/utils/model-utils';
export class ModelValidAtion {

  public static removeTask(f: Flow, uid: string): void {
    const b = f.getBoxByChild(uid);
    if (b.taskSort.length <= 2) { throw new Error('至少要2個Task'); }
    const llk = b.getLastLink();
    if (llk.uid === uid) { throw new Error('最後一個Link無法移除'); }
    b.removeTask(uid);
  }

  public static removeBox(f: Flow, boxUid: string): void {
    if (f.getEntryBox().uid === boxUid) { throw new Error('進入點的BOX無法刪除'); }
    ModelUtils.removeBox(f, boxUid);

  }
}
