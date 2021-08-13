import { FlowState } from "./flow-pool-info";

export class JobRunInfo {

  public allCount: number;
  public stateCounts: Object;
  public lastState: FlowState;
  public uid: string;
  public enable: boolean;
  public lastStartAt: Date;
  public name: string;
  public runVersion: number;
  public jobVersion: number;

  public getStateCount(st: FlowState): number {
    if (!this.stateCounts) return 0;
    if (!this.stateCounts.hasOwnProperty(st)) return 0;
    return this.stateCounts[st];
  }

  public isShowRunVersion(): boolean {
    if (!this.enable) return false;
    return this.jobVersion != this.runVersion;
  }

}
