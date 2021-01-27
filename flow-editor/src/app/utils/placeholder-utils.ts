import { StringUtils } from './string-utils';



export class PlaceholderUtils {




  public static genApachVar(c: string): string {
    return '${' + c + '}';
  }

  public static isApachVar(testt: string): boolean {
    testt = testt.trim();
    if (StringUtils.isBlank(testt)) return false;
    if (!testt.startsWith('${')) return false;
    if (!testt.endsWith('}')) return false;
    return true;
  }

  public static listLiteHolderKey(testt: string): RegExpMatchArray {
    const reg = /[!~$]{1}([\w . ]+)/g;
    return testt.match(reg);
  }

  public static trimLiteTag(k: string): string {
    return k.substring(1, k.length);
  }

  public static listApachHolderKey(testt: string): RegExpMatchArray {
    return PlaceholderUtils.listHolderKey('\\$\\{', '\\}', '[\\w . ! ~ $]', testt);
  }

  public static trimApachTag(k: string): string {
    return k.substring(2, k.length - 1);
  }

  public static listHolderKey(st: string, ed: string, content: string, t: string): RegExpMatchArray {
    const es = `${st}(${content}+)${ed}`;
    const rex = new RegExp(es, 'g');
    return t.match(rex);
  }

}
