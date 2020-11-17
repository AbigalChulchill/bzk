import { ClassType } from 'class-transformer/ClassTransformer';
import { plainToClass } from 'class-transformer';


export class CommUtils {



  public static delay(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  public static makeAlphanumeric(lengthOfCode: number): string {
    return this.makeRandom(lengthOfCode, '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ');
  }

  public static makeRandom(lengthOfCode: number, possible: string): string {
    let text = '';
    for (let i = 0; i < lengthOfCode; i++) {
      text += possible.charAt(Math.floor(Math.random() * possible.length));
    }
    return text;
  }


  public static mergeRecursive(tar: object, src: object): object {
    for (const p of Object.keys(src)) {
      if (!tar.hasOwnProperty(p)) { continue; }
      // Property in destination object set; update its value.
      if (src[p].constructor === Object) {
        tar[p] = CommUtils.mergeRecursive(tar[p], src[p]);

      } else {
        tar[p] = src[p];
      }
    }
    return tar;
  }

  public static replaceByVal(tar: object, org: any, newV: any): void {
    for (const p of Object.keys(tar)) {
      if (tar[p] === org) {
        tar[p] = newV;
      } else if (tar[p] instanceof Object) {
        CommUtils.replaceByVal(tar[p], org, newV);
      }
    }
  }

  public static removeArray(ary: Array<any>, f: (e: any) => boolean): void {
    const idx = ary.findIndex(f);
    ary.splice(idx, 1);
  }

  public static clone(d: any): any {
    const j = JSON.stringify(d);
    const no = JSON.parse(j);
    return plainToClass(d.constructor, no);
  }



}
