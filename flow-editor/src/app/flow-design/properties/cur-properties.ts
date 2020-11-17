import { Prop, PropClazzArgs, PropUtils } from 'src/app/utils/prop-utils';

export class CurProperties {


  public source: Source;
  public props = new Array<Prop>();
  public clazzInfo: PropClazzArgs;
  private onChange: () => void;

  public constructor(oc: () => void) {
    this.onChange = oc;
  }

  public setSource(t: Source): void {
    this.source = t;
    this.props = PropUtils.getInstance().list(this.source.target.obj);
    this.clazzInfo = PropUtils.getInstance().getPropClazzArgs(this.source.target.obj);
    this.onChange();
  }

  public setTarget(t: STar): void {
    this.setSource(new Source(t, null));
  }

  public nextTarget(t: STar): void {
    this.setSource(new Source(t, this.source));
  }

  public preview(): void {
    this.setSource(new Source(this.source.preview.target, this.source.preview.preview));
  }

  public hasTarget(): boolean {
    return this.source && this.source.target != null;
  }

  public hasPreview(): boolean {
    return this.source && this.source.preview != null;
  }

  public listPreviews(): Array<Source> {
    const ans = new Array<Source>();
    for (let s = this.source.preview; s; s = s.preview) {
      ans.push(s);
    }
    return ans;
  }

  public replaceTarget(org: object, newV: object): void {
    for (let cs = this.source; cs; cs = cs.preview) {
      if (cs.target.obj === org) {
        cs.target.obj = newV;
      }
    }
    this.props = PropUtils.getInstance().list(this.source.target.obj);
  }

}



export class Source {
  public target: STar;
  public preview: Source;

  public constructor(t: STar, p: Source) {
    this.target = t;
    this.preview = p;
  }

  public getTarClazzArgs(): PropClazzArgs {
    return PropUtils.getInstance().getPropClazzArgs(this.target.obj);
  }


}

export class STar {

  public obj: object;
  public key: string;

}

