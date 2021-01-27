import { VarKey } from "../model/var-key";
import { Type } from 'class-transformer';
export class Meta {
}

export interface TextProvide {

  getStr(): string;

  setStr(d: string): void;

}


export class ObjTextProvide implements TextProvide {

  private obj: object;
  private field: string;

  public static gen(ino: object, f: string): ObjTextProvide {
    return new ObjTextProvide(ino, f);
  }

  private constructor(o: object, f: string) {
    this.obj = o;
    this.field = f;
  }

  getStr(): string {
    return this.obj[this.field];
  }
  setStr(d: string): void {
    this.obj[this.field] = d;
  }

}

export class KV {
  public key: string;
  public val: any;
}


export class BaseVar extends Object {

  public static pathDot = '.';

  public listFlatKV(): Array<KV> {
    const ans = new Array<KV>();
    this.parsePathValueRecursively('', this, ans);
    return ans;
  }

  public parsePathValueRecursively(allpath: string, obj: object, ans: Array<KV>) {
    for (const k of Object.keys(obj)) {
      const v = obj[k];
      const path = allpath + k;
      if (typeof v === 'object') {
        this.parsePathValueRecursively(path + BaseVar.pathDot, v, ans);
      } else {
        ans.push({
          key: path,
          val: v
        });
      }
    }
  }

}

export class VarKeyReflect {
  public srcKey = '';
  @Type(() => VarKey)
  public toKey = new VarKey();
}
