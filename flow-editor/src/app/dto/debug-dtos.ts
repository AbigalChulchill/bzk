import { BaseVar, Flow } from '../model/flow';

export class DebugDtos {
}

export class ActionDebugData {
  public uid: string;
  public flow: Flow;
  public flowVar: BaseVar;
  public boxVar: BaseVar;
}
