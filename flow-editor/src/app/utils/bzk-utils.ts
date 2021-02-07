import { ReflectUtils } from './reflect-utils';
import { BzkObj, OType } from './../model/bzk-obj';
import 'reflect-metadata';
import { plainToClass } from 'class-transformer';
import { ClassType } from 'class-transformer/ClassTransformer';
const metadataOTypeClassKey = 'OTypeClass';
const otypeMap = new Map<string, any>();

// tslint:disable-next-line: typedef
export function OTypeClass(value: OType) {
  return (target: any) => {
    Reflect.defineMetadata(metadataOTypeClassKey, value, target);
    otypeMap.set(value.clazz, target);
    // console.log(value.clazz + ':' + target);
  };
}

export class BzkUtils {

  public static getOTypeInfo(clz: any): OType {
    const o = Reflect.getMetadata(metadataOTypeClassKey, clz);
    return o;
  }

  public static fitClzz<T>(cls: ClassType<T>, o: OType): T {
    const inPo = plainToClass(cls, o);
    return BzkUtils.fixType(inPo) as T;
  }

  public static fixType(o: any): any {
    let inPo = o;
    if (o instanceof OType) {
      const cls = otypeMap.get(o.clazz);
      inPo = plainToClass(cls, o);
    }
    if (!inPo) { return inPo; }
    const keys = Object.keys(inPo);
    for (const k of keys) {
      const ko = inPo[k];
      if (typeof ko === 'object') {
        inPo[k] = BzkUtils.fixType(ko);
      }
    }
    return inPo;
  }

  public static listOtypeKeys(clz: any): Array<string> {
    const ans = new Array<string>();
    const cldClzs = this.listChilds(clz);
    for (clz of cldClzs) {
      const oi = this.getOTypeInfo(clz);
      if (!oi) { continue; }
      ans.push(oi.clazz);
    }
    return ans;
  }

  public static listChilds(clz: any): Array<any> {
    const ans = new Array<any>();
    for (const k of otypeMap.keys()) {
      const co = otypeMap.get(k);
      const cplist = ReflectUtils.listParents(co);
      if (cplist.includes(clz)) {
        ans.push(co);
      }
    }
    return ans;
  }

  public static findByUid(bo: object, iuid: string): BzkObj {
    if (bo instanceof BzkObj && bo.uid === iuid) {
      return bo;
    }
    const ks = Object.keys(bo);
    for (const k of ks) {
      const ko = bo[k];
      if (!ko) { continue; }
      if (ko instanceof Array) {
        const clans = this.findObjByUidAtList(ko, iuid);
        if (clans) { return clans; }
      } else if (typeof ko === 'object') {
        console.log(JSON.stringify(ko));
        const cans = BzkUtils.findByUid(ko, iuid);
        if (cans) { return cans; }
      }
    }
    return null;
  }

  public static findObjByUidAtList(bos: Array<any>, iuid: string): BzkObj {
    for (const o of bos) {
      const ans = this.findByUid(o, iuid);
      if (ans) { return ans; }
    }
    return null;
  }

  public static getTypeByClazz(clz: string): any {
    return otypeMap.get(clz);
  }


  public static activity(clazz: any): void {
    console.log(clazz);
  }


}

