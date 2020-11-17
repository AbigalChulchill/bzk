export class ReflectUtils {


  public static listParents(clz: any): Array<any> {
    const ans = new Array<any>();
    let inc = ReflectUtils.getParentFromClz(clz);
    ans.push(inc);
    while (inc !== Object) {
      inc = ReflectUtils.getParentFromClz(inc);
      ans.push(inc);
    }
    return ans;
  }

  public static getParentFromClz<T>(clz: any): any {
    const o = new clz();
    return ReflectUtils.getParentFromObj(o);
  }

  public static getParentFromObj(tar: any): any {
    const obj = Object.getPrototypeOf(tar);
    return obj.__proto__.constructor;
  }



}
