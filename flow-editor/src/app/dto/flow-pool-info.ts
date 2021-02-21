import { Link } from "../model/box";
import { Flow } from "../model/flow";

export class FlowPoolInfo {

  public flow: Flow;
  public runInfos: Array<RunInfo>;

}



export class RunInfo {

  public uid: string;
  public state = FlowState.Pedding;
  public endLink: Link;
  public startAt = '-';
  public endAt = '-';

}

export enum FlowState {
  Pedding = 'Pedding', Running = 'Running', Done = 'Done', Fail = 'Fail'
}
