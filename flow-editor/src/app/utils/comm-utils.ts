import { ClassType } from 'class-transformer/ClassTransformer';
import { plainToClass } from 'class-transformer';
import * as CryptoJS from 'crypto-js';
import { DateTimeFormatter, ZonedDateTime, ZoneId } from '@js-joda/core';

export class CommUtils {


  public static getZoneDateStr(ds: string,zoneId:string): string {
    return ZonedDateTime.parse(ds).withZoneSameInstant(ZoneId.of(zoneId)).format(DateTimeFormatter.ofPattern('yyyy-MM-dd HH:mm:ss'));
  }

  public static delay(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  public static delayCall(ms, a: () => void): void {
    setTimeout(cb => { a(); }, ms)
  }


  public static decryptAES(decPassword: string, encryptText: string): string {
    const key = CryptoJS.enc.Utf8.parse(decPassword.trim()); // 秘钥
    const encryptedBase64Str = encryptText.trim();
    const decryptedData = CryptoJS.AES.decrypt(encryptedBase64Str, key, {
      mode: CryptoJS.mode.ECB,
      padding: CryptoJS.pad.Pkcs7
    });
    const decryptedStr = decryptedData.toString(CryptoJS.enc.Utf8);
    return decryptedStr;
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

  public static cleanObj(o: any): void {
    for (const p of Object.keys(o)) {
      delete o[p];
    }
  }

  public static addProps(tar: any, src: any): void {
    for (const p of Object.keys(src)) {
      tar[p] = src[p];
    }
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

  public static convertByJson<T>(clz: ClassType<T>, json: string): T {
    const no = JSON.parse(json);
    return plainToClass(clz, no);
  }

  public static pushUnique<T>(list: Array<T>, e: T):boolean {
    if (list.includes(e)) return false;
    list.push(e);
    return true;
  }


}
