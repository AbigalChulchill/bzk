import { ChronoUnit } from './enums';
import { OTypeClass } from '../utils/bzk-utils';
import { PropClazz, PropEnums, PropInfo, PropType } from '../utils/prop-utils';
import { OType } from './bzk-obj';
export  class Entry extends OType  {
  public boxUid: string;
}

@OTypeClass({
  clazz: 'net.bzk.flow.model.Entry$FixedRateEntry'
})
@PropClazz({
  title: 'FixedRateEntry'
})
export class FixedRateEntry extends Entry {

  @PropInfo({
    title: 'period',
    type: PropType.Number
  })
  public period: number;
  @PropInfo({
    title: 'unit',
    type: PropType.Enum
  })
  @PropEnums( Object.keys(ChronoUnit))
  public unit: ChronoUnit;
  @PropInfo({
    title: 'initialDelay',
    type: PropType.Number
  })
  public initialDelay: number;

}


