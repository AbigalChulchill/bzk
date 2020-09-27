import { LanguageService } from '../service/language.service';
import 'reflect-metadata';
// https://magiclen.org/typescript-decorator/
// https://stackoverflow.com/questions/41144335/get-list-of-attribute-decorators-in-typescript


const metadataKey = 'PropInfo';

// tslint:disable-next-line: typedef
export function PropInfo(value: PropInfoArgs) {
  return (target: any, propertyKey: string) => {
    Reflect.defineMetadata(metadataKey, value, target, propertyKey);
  };
}

export interface PropInfoArgs {
  titleI18nKey: string;
  title: string;
  type: PropType;

}

export enum PropType {
  Unknown = 'Unknown', Text = 'Text', Label = 'Label', Number = 'Number', Boolean = 'Boolean', Enum = 'Enum'
}

export class Prop {

  public type: PropType;
  public title: string;

  valGeter: () => any;
  valSeter: (a: any) => void;

  public get val(): any {
    return this.valGeter();
  }

  public set val(a: any) {
    this.valSeter(a);
  }




}

export class PropUtils {

  private static instance: PropUtils;
  private language: LanguageService;

  private constructor(l: LanguageService) {
    this.language = l;
  }

  public static init(l: LanguageService): void {
    this.instance = new PropUtils(l);
  }

  public static getInstance(): PropUtils {
    return this.instance;
  }

  public getPropArg(obj: any, field: string): PropInfoArgs {
    const o = Reflect.getMetadata(metadataKey, obj, field);
    return o;
  }

  public async list(tar: object): Promise<Array<Prop>> {
    const ans = new Array<Prop>();
    const keys = Object.keys(tar);
    for (const k of keys) {
      const p = (await this.gen(k, tar));
      if (!p) { continue; }
      ans.push(p);
    }
    return ans;
  }

  private async gen(key: string, tar: object): Promise<Prop> {
    const ans = new Prop();
    const p: PropInfoArgs = this.getPropArg(tar, key);
    if (!p) { return null; }
    ans.title = await this.language.optI18n(p.titleI18nKey, p.title);
    ans.type = p.type;

    ans.valGeter = () => tar[key];
    ans.valSeter = (v) => tar[key] = v;
    return ans;
  }







}
