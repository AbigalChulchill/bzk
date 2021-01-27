import { Type } from "class-transformer";
import { PropClazz, PropInfo, PropType } from "../utils/prop-utils";
import { VarKey } from "./var-key";

@PropClazz({
  title: 'Transition',
  exView: 'TransitionComponent'
})
export class Transition {
  public toBox='';
  public endTag='END';
  @PropInfo({
    title: 'Return Keys',
    type:PropType.List,
    child:{
      type: PropType.Custom,
      customView: 'VarKeyComponent',
      newObj: new VarKey()
    }

  })
  @Type(() => VarKey)
  public endResultKeys = new Array<VarKey>();
}
