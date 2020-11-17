export class StringUtils {
  public static isBlank(str: string): boolean {
    if (!str) { return true; }
    return (!str || /^\s*$/.test(str));
  }

  public static opt(str: string, d: string): string {
    if (StringUtils.isBlank(str)) { return d; }
    return str;
  }

}
