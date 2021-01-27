import { VarLv, VarLvF } from "./pojo/enums";

export class VarKey {
  public lv = VarLv.not_specify;
  public key = '';

  public getExpress(): string {
    return VarLvF.getPrefix(this.lv) + this.key;
  }

  public setByExpress(s: string): void {
    const r = VarLvF.parse(s);
    if (!r) {
      this.lv = VarLv.not_specify;
      this.key = s;
      return;
    }
    this.lv = r.lv;
    this.key = r.key;
  }

}
