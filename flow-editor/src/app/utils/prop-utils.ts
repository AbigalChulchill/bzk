import { ClassType } from 'class-transformer/ClassTransformer';
import { LanguageService } from '../service/language.service';
import 'reflect-metadata';
// https://magiclen.org/typescript-decorator/
// https://stackoverflow.com/questions/41144335/get-list-of-attribute-decorators-in-typescript


const metadataKey = 'PropInfo';
const metadataPropEnumsKey = 'PropEnums';
const metadataPropClazzKey = 'PropClazz';

// tslint:disable-next-line: typedef
export function PropClazz(value: PropClazzArgs) {
  return (target: any) => {
    Reflect.defineMetadata(metadataPropClazzKey, value, target);
  };
}


// tslint:disable-next-line: typedef
export function PropInfo(value: PropInfoArgs) {
  return (target: any, propertyKey: string) => {
    Reflect.defineMetadata(metadataKey, value, target, propertyKey);
  };
}

export type EnumValuesProvider = () => Array<string>;
// tslint:disable-next-line: typedef
export function PropEnums(value: Array<string> | EnumValuesProvider) {
  return (target: any, propertyKey: string) => {
    Reflect.defineMetadata(metadataPropEnumsKey, value, target, propertyKey);
  };
}

export interface PropClazzArgs {
  title: string;
  exView?: string;
}

export interface ClazzExComponent {
  init(d: any): void;
  getData(): any;
}

export type UpdatePredicate = (p: Prop, oldV: any) => boolean;

export class PropInfoArgs {
  title: string;
  type: PropType;
  listChildType?: PropType = PropType.Object;
  listChildNewObj?: any;
  autocompleteList?: Array<string> = [];
  updatePredicate?: UpdatePredicate = (p, n) => true;

}

export class Prop {
  public info: PropInfoArgs;
  public field: string;

  public object: object;

  valGeter: () => any;
  valSeter: (a: any) => void;

  public get updatePredicate(): UpdatePredicate { return this.info.updatePredicate; }
  public get title(): string { return this.info.title; }
  public get type(): PropType { return this.info.type; }

  public get val(): any {
    return this.valGeter();
  }

  public set val(a: any) {
    const oldv = this.valGeter();
    this.valSeter(a);
    if (this.updatePredicate && !this.updatePredicate(this, oldv)) {
      this.valSeter(oldv);
    }
  }
}


export enum PropType {
  Unknown = 'Unknown',
  Text = 'Text',
  Label = 'Label',
  Number = 'Number',
  Boolean = 'Boolean',
  Enum = 'Enum',
  Object = 'Object',
  List = 'List',
  MultipleText = 'MultipleText'
}


export class PropUtils {

  private static instance: PropUtils;
  private language: LanguageService;
  private exViewMap = new Map<string, ClassType<ClazzExComponent>>();

  private constructor(l: LanguageService) {
    this.language = l;
  }

  public static init(l: LanguageService): void {
    this.instance = new PropUtils(l);
  }

  public static getInstance(): PropUtils {
    return this.instance;
  }

  public getExViewClazz(k: string): ClassType<ClazzExComponent> {
    return this.exViewMap.get(k);
  }

  public bindExView(k: string, c: ClassType<ClazzExComponent>): void {
    this.exViewMap.set(k, c);
  }

  public getPropArg(obj: any, field: string): PropInfoArgs {
    const o = Reflect.getMetadata(metadataKey, obj, field);
    return o;
  }



  public getPropClazzArgs(obj: any): PropClazzArgs {
    return Reflect.getMetadata(metadataPropClazzKey, obj.constructor);
  }

  public listPropEnums(p: Prop): Array<string> {
    const o = Reflect.getMetadata(metadataPropEnumsKey, p.object, p.field);
    if (o instanceof Array) {
      return o;
    } else {
      const op: EnumValuesProvider = o;
      return op();
    }
  }

  public list(tar: object): Array<Prop> {
    const ans = new Array<Prop>();
    const keys = Object.keys(tar);
    for (const k of keys) {
      const p = (this.gen(k, tar));
      if (!p) { continue; }
      ans.push(p);
    }
    return ans;
  }

  private gen(key: string, tar: object): Prop {
    const ans = new Prop();
    const p: PropInfoArgs = this.getPropArg(tar, key);
    if (!p) { return null; }
    ans.info = p;
    ans.field = key;
    ans.object = tar;
    ans.valGeter = () => tar[key];
    ans.valSeter = (v) => tar[key] = v;
    return ans;
  }


}
