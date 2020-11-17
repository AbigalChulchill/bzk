import { DtoVarQuery } from './dto-var-query';


export class Condition {

  public clazz: string;
  public kind: ConKind;
  public next: Condition;

}


export enum ConKind {

  NONE = 'NONE', AND = 'AND', OR = 'OR'

}



export enum NumCheckType {
  equal = 'equal', greater = 'greater', greater_equal = 'greater_equal', lessthan = 'lessthan', lessthan_equal = 'lessthan_equal'
}

export class Val {
  public clazz: string;
}

export class RefVal extends Val {
  public query: DtoVarQuery;
}

export class TxtVal extends Val {
  public val: string;
}

export enum TxtCheckType {
  equal, startsWith, endsWith, contains
}

export class ConditionInclude extends Condition {
  public include: Condition;
}

export class ConditionTxt extends Condition {
  public left: Val;
  public right: Val;
  public type: TxtCheckType;
}

export class ConditionNum extends Condition {

  public left: Val;
  public right: Val;
  public type: NumCheckType;

}
