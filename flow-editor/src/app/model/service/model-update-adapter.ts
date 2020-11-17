import { Link, Box } from './../box';
import { Action } from './../action';
import { BzkUtils } from 'src/app/utils/bzk-utils';
import { CommUtils } from 'src/app/utils/comm-utils';
import { Prop } from 'src/app/utils/prop-utils';
import { OType } from '../bzk-obj';
import { Flow } from '../flow';

export type LinkCB = (input: Box) => void;

export interface ModelUpdate {
  onRefleshCart(): void;
  onClazzUpdate(newObj: OType, oldObj: OType): void;
  getFlow(): Flow;
  onLinkBox(cb: LinkCB): void;
}

export class ModelUpdateAdapter {


  private static instance = new ModelUpdateAdapter();

  private currentUpdater: ModelUpdate;

  private constructor() { }

  public static getInstance(): ModelUpdateAdapter {
    return ModelUpdateAdapter.instance;
  }

  public getFlow(): Flow { return this.currentUpdater.getFlow(); }

  public get updater(): ModelUpdate { return this.currentUpdater; }

  public onRefleshCart(): boolean {
    this.currentUpdater.onRefleshCart();
    return true;
  }

  public onClazzUpdate(p: Prop, oldV: any): boolean {
    const obj: OType = p.object as OType;
    const newClz = obj.clazz;
    const TC = BzkUtils.getTypeByClazz(newClz);
    const no = new TC();
    CommUtils.mergeRecursive(no, obj);
    no.clazz = obj.clazz;
    this.currentUpdater.onClazzUpdate(no, obj);
    return true;
  }


  public setCurUpdater(m: ModelUpdate): void {
    this.currentUpdater = m;
  }




}
