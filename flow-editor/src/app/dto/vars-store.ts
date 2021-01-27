import { PropClazz } from '../utils/prop-utils';
import { Type, plainToClass } from 'class-transformer';
import { BaseVar, KV } from '../infrastructure/meta';



@PropClazz({
  title: 'VarsStore',
  exView: 'VarStoreComponent'
})
export class VarsStore extends Object {
  public getVars(key: string): BaseVar {
    const ans = this[key];
    return plainToClass(BaseVar, ans);
  }

  public setVars(key: string, b: BaseVar): void {
    this[key] = b;
  }

  public listKeys(): Array<string> {
    return Object.keys(this);
  }

  public listAllFlatKV(): Array<KV> {
    const ans = new Array<KV>();
    for (const k of this.listKeys()) {
      const vs = this.getVars(k);
      vs.listFlatKV().forEach(kv => ans.push(kv));
    }
    return ans;
  }

}
