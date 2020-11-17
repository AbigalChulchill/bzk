import { BaseVar } from './../model/flow';
import { PropClazz } from '../utils/prop-utils';
import { Type } from 'class-transformer';



@PropClazz({
  title: 'VarsStore',
  exView: 'VarStoreComponent'
})
export class VarsStore extends Object {
  public getVars(key: string): BaseVar {
    return this[key];
  }

  public setVars(key: string, b: BaseVar): void {
    this[key] = b;
  }

  public listKeys(): Array<string> {
    return Object.keys(this);
  }

}
