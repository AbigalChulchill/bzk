import { JsonEditorComponent, ReadJsonProvide, SimpleJsonProvide } from './../uikit/json-editor/json-editor.component';
import { DialogService } from './../uikit/dialog.service';
import { Injectable } from '@angular/core';
import { LoadingService } from '../service/loading.service';
import { LoadLastMark, ModifyingFlowService } from '../service/modifying-flow.service';
import { ToastService } from '../service/toast.service';
import { saveAs } from 'file-saver';

@Injectable({
  providedIn: 'root'
})
export class FlowDesignMenuService {


  constructor(
    public modifyingFlow: ModifyingFlowService,
    private dialog: DialogService,
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

  public showModelJson(): void {
    console.log('showModelJson');
    const df= this.dialog.open(JsonEditorComponent);
    const jsonC:JsonEditorComponent = df.componentInstance;
    jsonC.fullWidth = true;
    jsonC.setData(SimpleJsonProvide.gen(this.modifyingFlow.modelobs.getModel()));
  }

  public exportFile():void{
    const m= this.modifyingFlow.modelobs.getModel();
    var blob = new Blob([JSON.stringify(m,null,4)], {type: "application/json;charset=utf-8"});
    saveAs(blob, `${m.uid}.json` );
  }


  public async register(): Promise<void> {
    await this.waitLoad(async () => {
      await this.modifyingFlow.registerRemote();
    });
  }

  public testRun():void{
    this.modifyingFlow.testFlow();
    this.toast.twinkle({
      title: 'RUN!',
      msg: 'test submmited'
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
