import { BzkUtils, OTypeClass } from '../utils/bzk-utils';
import { PropClazz, PropEnums, PropInfo, PropType } from '../utils/prop-utils';
import { OType } from './bzk-obj';
import { DtoVarQuery } from './dto-var-query';
import { Polyglot, VarLv } from './pojo/enums';
import { ModelUpdateAdapter } from './service/model-update-adapter';
import { Type } from 'class-transformer';

export enum ConKind {

  NONE = 'NONE', AND = 'AND', OR = 'OR'

}

export class Condition extends OType {

  // @PropInfo({
  //   title: 'kind',
  //   type: PropType.Enum,
  // })
  // @PropEnums(Object.keys(ConKind))
  public kind = ConKind.NONE;
  @Type(() => Condition)
  public next: Condition;

  @PropInfo({
    title: 'clazz',
    type: PropType.Unknown,
  })
  public clazz = '';
}


export enum NumCheckType {
  equal = 'equal', greater = 'greater', greater_equal = 'greater_equal', lessthan = 'lessthan', lessthan_equal = 'lessthan_equal', not_equal = 'not_equal'
}


export enum TxtCheckType {
  equal = 'equal', startsWith = 'startsWith', endsWith = 'endsWith', contains = 'contains'
}

export enum TimeCheckType {
  After = 'After', Before = 'Before', Equal = 'Equal'
}

@PropClazz({
  exView: 'ConditionIncludeComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Condition$ConditionInclude'
})
export class ConditionInclude extends Condition {
  @Type(() => Condition)
  public include: Condition;

  public static gen(): ConditionInclude {
    const na = new ConditionInclude();
    na.clazz = BzkUtils.getOTypeInfo(na.constructor).clazz;
    na.include = ConditionNum.gen();
    return na;
  }

}

@PropClazz({
  exView: 'ConditionTxtComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Condition$ConditionTxt'
})
export class ConditionTxt extends Condition {
  public left = '';
  public right = '';
  public type = TxtCheckType.contains;
  @PropInfo({
    title: 'not',
    type: PropType.Boolean
  })
  public not = false;

  public static gen(): ConditionTxt {
    const na = new ConditionTxt();
    na.clazz = BzkUtils.getOTypeInfo(na.constructor).clazz;
    return na;
  }

}


@PropClazz({
  exView: 'ConditionTimeComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Condition$ConditionTime'
})
export class ConditionTime extends Condition {
  public left = '';
  public right = '';
  public type = TimeCheckType.After;
  @PropInfo({
    title: 'not',
    type: PropType.Boolean
  })
  public not = false;

  public static gen(): ConditionTime {
    const na = new ConditionTime();
    na.clazz = BzkUtils.getOTypeInfo(na.constructor).clazz;
    return na;
  }

}


@PropClazz({
  exView: 'ConditionNumComponent'
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Condition$ConditionNum'
})
export class ConditionNum extends Condition {

  public left = '0';
  public right = '0';

  public type = NumCheckType.equal;

  public static gen(): ConditionNum {
    const na = new ConditionNum();
    na.clazz = BzkUtils.getOTypeInfo(na.constructor).clazz;
    return na;
  }
}


@PropClazz({
})
@OTypeClass({
  clazz: 'net.bzk.flow.model.Condition$ConditionCode'
})
export class ConditionCode extends Condition {

  @PropInfo({
    title: 'polyglot',
    type: PropType.Enum
  })
  @PropEnums(Object.keys(Polyglot))
  public polyglot = Polyglot.js;
  @PropInfo({
    title: 'code',
    type: PropType.MultipleText
  })
  public code = '';

  public static gen(): ConditionCode {
    const na = new ConditionCode();
    na.clazz = BzkUtils.getOTypeInfo(na.constructor).clazz;
    return na;
  }
}
