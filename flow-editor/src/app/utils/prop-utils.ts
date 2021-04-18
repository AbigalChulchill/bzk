import { PropRefVal } from './prop-ref-val';
import { CommUtils } from './comm-utils';
import { StringUtils } from './string-utils';
import { ClassType } from 'class-transformer/ClassTransformer';
import { LanguageService } from '../service/language.service';
import 'reflect-metadata';
import { plainToClass } from 'class-transformer';
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
  title?: string;
  exView?: string;
}

export interface ClazzExComponent {
  init(d: any, mataInfo: any): void;
}

export type UpdatePredicate = (p: Prop, oldV: any) => boolean;

export class PropInfoArgs {
  title?: string;
  type: PropType;
  hide?: boolean;
  child?: PropInfoArgs;
  newObj?: any;
  customView?: string;
  customViewFolded?: boolean = true;
  autocompleteFuncKey?: string;
  autocompleteList?: string[];
  objectSamePage?: boolean;
  updatePredicate?: UpdatePredicate = (p, n) => true;

}

export class Prop {
  public info: PropInfoArgs;
  public field: any;
  public object: any;
  public refVal: PropRefVal;
  public onChange = ()=>{};


  public get title(): string { return this.info.title; }
  public get type(): PropType { return this.info.type; }


  public getOrgVal(): any {
    return this.object[this.field];
  }

  public get val(): any {
    if (this.refVal) return this.refVal.val;
    return this.object[this.field];
  }

  public set val(a: any) {
    const oldv = this.object[this.field];
    this.setToVal(a);
    this.onChange();
    if (this.info.updatePredicate && !this.info.updatePredicate(this, oldv)) {
      this.setToVal(oldv);
    }
  }

  private setToVal(v: any): void {
    if (this.refVal) {
      this.refVal.val = v;
      return;
    }
    this.setOrgVal(v);
  }

  public setOrgVal(v: any): void {
    this.object[this.field] = v;
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
  Map = 'Map',
  List = 'List',
  MultipleText = 'MultipleText',
  Custom = 'Custom'
}




export class PropUtils {

  private static instance: PropUtils;
  private language: LanguageService;
  private exViewMap = new Map<string, ClassType<ClazzExComponent>>();
  private exAutoText = new Map<string, () => Array<string>>();

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

  public bindExAutoText(k: string, f: () => Array<string>): void {
    this.exAutoText.set(k, f);
  }

  public getExAutoText(k: string): () => Array<string> {
    return this.exAutoText.get(k);
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
      if (p.info.hide) { continue; }
      ans.push(p);
    }
    return ans;
  }

  public gen(key: string, tar: object): Prop {
    const p: PropInfoArgs = this.getPropArg(tar, key);
    if (!p) { return null; }
    const ans = this.genHasInfo(key, tar, p);
    return ans;
  }

  public genHasInfo(key: string, tar: object, p: PropInfoArgs): Prop {
    const ans = new Prop();
    ans.info = p;
    ans.field = key;
    ans.object = tar;
    return ans;
  }









}
