import { CommUtils } from 'src/app/utils/comm-utils';
import { PlaceholderUtils } from './placeholder-utils';
import { ModelUtils } from './model-utils';
import { Flow } from './../model/flow';
import { Condition } from '../model/condition';
export class VarUtils {

  public static listVarKeyByLinks(f: Flow): Array<string> {
    const ans = new Array<string>();
    const cods = new Array<Condition>();
    ModelUtils.listAllLinks(f).forEach(l => {
      l.transition.endResultKeys.forEach(k=> CommUtils.pushUnique(ans, k.key));
      cods.push(l.condition);
    });
    cods.forEach(l => {
      const json = JSON.stringify(l);
      const ls = VarUtils.listConditionKey(json);
      if (ls && ls.length > 0) ls.forEach(l => CommUtils.pushUnique(ans, VarUtils.trimConditionTag(l)));

    });
    return ans;
  }



  private static trimConditionTag(s: string): string {
    s = '{' + s + '}';
    return JSON.parse(s).key;
  }

  private static listConditionKey(j: string): RegExpMatchArray {
    const reg = /"key" ?: ?"[\w .]+"/g;
    return j.match(reg);
  }

  public static listVarKeyByAction(f: Flow): Array<string> {
    const ans = new Array<string>();
    ModelUtils.listAllAction(f).forEach(l => {
      const ls = l.listVarKey();
      if (ls && ls.length > 0) ls.forEach(l => CommUtils.pushUnique(ans, l));
    });
    return ans;
  }


  public static listByFlowVar(f:Flow): Array<string> {
    const ans = new Array<string>();
    const vkvs= f.vars.listFlatKV();
    vkvs.forEach(kv=>{
      ans.push(kv.key);
    });
    return ans;
  }


  public static listVarKeyByFlow(f: Flow): Array<string> {
    const ans = VarUtils.listVarKeyByAction(f);
    VarUtils.listVarKeyByLinks(f).forEach(l => CommUtils.pushUnique(ans, l));
    VarUtils.listByFlowVar(f).forEach(l => CommUtils.pushUnique(ans, l));
    return ans;
  }

}
