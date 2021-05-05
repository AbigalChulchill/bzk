export class VarLvF {

  public static getPrefix(l: VarLv): string {
    switch (l) {
      case VarLv.not_specify: return '$';
      case VarLv.run_box: return '!';
      case VarLv.run_flow: return '~';
    }
  }

  public static checkByPrefix(s: string, def: VarLv = null): VarLv {
    if (s.startsWith(this.getPrefix(VarLv.run_box))) { return VarLv.run_box; }
    if (s.startsWith(this.getPrefix(VarLv.run_flow))) { return VarLv.run_flow; }
    if (s.startsWith(this.getPrefix(VarLv.not_specify))) { return VarLv.not_specify; }
    return def;
  }

  public static parse(s: string, def: VarLv = null): { lv: VarLv, key: string } {
    const lv = VarLvF.checkByPrefix(s, def);
    if (!lv) return null;
    return {
      lv,
      key: s.substring(1, s.length)
    };
  }

  public static isVarLvExp(s: string): boolean {
    const lv = this.checkByPrefix(s);
    return lv !== null;
  }

}

export enum DataType {
  string = 'string', number = 'number', Boolean = 'Boolean', NULL = 'NULL', object = 'object', array = 'array', NotSupport = 'NotSupport',
}

export enum Polyglot {
  js = 'js', R = 'R', ruby = 'ruby', python = 'python'
}

export enum VarLv {
  not_specify = 'not_specify', run_flow = 'run_flow', run_box = 'run_box'
}

export enum ConvertMethod {
  ToJSONText = 'ToJSONText'
}


export enum RunState {
  BoxStart = 'BoxStart', BoxLoop = 'BoxLoop', BoxLoopDone = 'BoxLoopDone', EndFlow = 'EndFlow', LinkTo = 'LinkTo',
  StartAction = 'StartAction', EndAction = 'EndAction', ActionCall = 'ActionCall',
  ActionCallFail = 'ActionCallFail', ActionCallWarn = 'ActionCallWarn', ActionResult = 'ActionResult', WhileLoopBottom = 'WhileLoopBottom',
  ConditionFail = 'ConditionFail',ModelReplaced='ModelReplaced',PolyglotExecute='PolyglotExecute'

}


