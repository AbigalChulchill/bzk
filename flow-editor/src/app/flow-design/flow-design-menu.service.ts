import { Injectable } from '@angular/core';
import { LoadingService } from '../service/loading.service';
import { LoadLastMark, ModifyingFlowService } from '../service/modifying-flow.service';
import { ToastService } from '../service/toast.service';

@Injectable({
  providedIn: 'root'
})
export class FlowDesignMenuService {


  constructor(
    public modifyingFlow: ModifyingFlowService,
    private loading: LoadingService,
    private toast: ToastService
  ) { }


  public async saveAsNew(): Promise<void> {
    await this.waitLoad(async () => {
      await this.modifyingFlow.saveAsNew();
    });
  }

  public async updateRemote(): Promise<void> {
    await this.waitLoad(async () => {
      await this.modifyingFlow.updateRemote();
    });
  }

  public async register(): Promise<void> {
    await this.waitLoad(async () => {
      await this.modifyingFlow.registerRemote();
    });
  }

  private async waitLoad(task: () => Promise<void>): Promise<void> {
    const t = this.loading.show();
    await task();
    this.loading.dismiss(t);
    this.toast.twinkle({
      title: 'ok',
      msg: 'save ok'
    });
  }

  public getLast(): LoadLastMark {
    return this.modifyingFlow.getLastMark();
  }


}
