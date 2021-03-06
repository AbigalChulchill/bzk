import { VarService } from './../service/var.service';
import { ChronoUnit } from './enums';
import { OTypeClass } from '../utils/bzk-utils';
import { PropClazz, PropEnums, PropInfo, PropType } from '../utils/prop-utils';
import { OType } from './bzk-obj';
import { Condition, ConditionTxt } from './condition';
import { Type } from 'class-transformer';


export class Entry extends OType {
  public boxUid: string;

  @PropInfo({
    title: 'auto Register',
    type: PropType.Boolean,
  })
  public autoRegister = false;
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
  public period = 0;
  @PropInfo({
    title: 'unit',
    type: PropType.Enum
  })
  @PropEnums(Object.keys(ChronoUnit))
  public unit = ChronoUnit.HOURS;
  @PropInfo({
    title: 'initialDelay',
    type: PropType.Number
  })
  public initialDelay = 0;
  @PropInfo({
    title: 'cronExpression',
    type: PropType.Text
  })
  public cronExpression = '0 0/5 0 ? * *';

}

@OTypeClass({
  clazz: 'net.bzk.flow.model.Entry$PluginEntry'
})
@PropClazz({
  title: 'PluginEntry'
})
export class PluginEntry extends Entry {

  @PropInfo({
    title: 'requiredKeys',
    type: PropType.List,
    child: {
      title: 'Key',
      type: PropType.Text,
      autocompleteFuncKey: 'ListAllVarKeys',
      newObj: 'TODO',
    },
  })
  public requiredKeys = new Array<string>();

  @PropInfo({
    title: 'outputKeys',
    type: PropType.List,
    child: {
      title: 'Key',
      type: PropType.Text,
      autocompleteFuncKey: 'ListAllVarKeys',
      newObj: 'TODO',
    },
  })
  public outputKeys = new Array<string>();

}


@OTypeClass({
  clazz: 'net.bzk.flow.model.Entry$EventEntry'
})
@PropClazz({
  title: 'EventEntry'
})
export class EventEntry extends Entry {

  @PropInfo({
    title: 'condition',
    type: PropType.Custom,
    customView: 'ConditionComponent',
    customViewFolded: true
  })
  @Type(() => Condition)
  public condition: Condition = new ConditionTxt();

}
