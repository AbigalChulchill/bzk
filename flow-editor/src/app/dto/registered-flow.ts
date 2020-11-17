import { Flow } from '../model/flow';

export class RegisteredFlow {

  public flow: Flow;
  public runInfos: Array<RunInfo>;


}

export class RunInfo {

  public runUid: string;
  public state = FlowState.Pedding;
  public endTag: string;
  public endLinkUid: string;

}

export enum FlowState {
  Pedding = 'Pedding', Running = 'Running', Done = 'Done', Fail = 'Fail'
}
