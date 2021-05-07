import { Condition } from 'src/app/model/condition';
import { Prop, PropInfoArgs, PropType, PropUtils } from "./prop-utils";

export class ConditionUtils {

  public static getProp(label: string, condition: Condition): Prop {
    const ans = PropUtils.getInstance().genHasInfo(label, condition, this.genPropInfo(label));
    ans.info.hide = false;
    return ans;
  }

  public static genPropInfo(t: string): PropInfoArgs {
    return {
      title: t,
      hide: true,
      type: PropType.MultipleText,
      // TODO condition scriptble

    };
  }

}


