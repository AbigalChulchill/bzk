import { SavedFlowClientService } from './saved-flow-client.service';
import { FlowClientService } from './flow-client.service';
import { CommUtils } from './../utils/comm-utils';
import { SubFlowAction } from './../model/action';
import { ModelUtils } from 'src/app/utils/model-utils';
import { VarsStore } from './../dto/vars-store';
import { ModelObserve } from './../model/model-observe';
import { async } from '@angular/core/testing';
import { LoadingService } from './loading.service';
import { GithubService } from './github.service';
import { Flow } from './../model/flow';
import { Injectable } from '@angular/core';
import { HttpClientService } from './http-client.service';
import { plainToClass } from 'class-transformer';
import { PropUtils } from '../utils/prop-utils';
import { StringUtils } from '../utils/string-utils';
import { Gist } from '../dto/gist';

@Injectable({
  providedIn: 'root'
})
export class ModifyingFlowService {

  public static KEY_LAST = 'last-load';
  public modelobs: ModelObserve = new ModelObserve();

  constructor(
    private clientService: HttpClientService,
    private flowClient:FlowClientService,
    private savedFlowClient: SavedFlowClientService,
    private loading: LoadingService
  ) {

  }


  public async loadInit(): Promise<void> {
    const t = this.loading.show();
    const sl = await this.loadLast();
    if (!sl) {
      await this.loadDemo();
    }
    this.loading.dismiss(t);
  }

  public async loadDemo(): Promise<void> {
    const lm = await this.flowClient.getDemoModel();
    this.modelobs.setModel(lm);
    console.log(lm);
  }

  public async loadLast(): Promise<boolean> {
    const llm = this.getLastMark();
    if (!llm) { return null; }
    try {
      const lfm = await this.savedFlowClient.getByUid(llm.id).toPromise();
      this.modelobs.setModel(lfm.model);
      return true;
    } catch (ex) {
      console.log(ex);
      return false;
    }
  }

  public getLastMark(): LoadLastMark {
    const llms = localStorage.getItem(ModifyingFlowService.KEY_LAST);
    if (StringUtils.isBlank(llms)) { return null; }
    const llm: LoadLastMark = JSON.parse(llms);
    return llm;
  }


  public setTarget(f: Flow, llm: LoadLastMark | undefined): void {
    this.modelobs.setModel(plainToClass(Flow, f));
    if (!llm) {
      localStorage.removeItem(ModifyingFlowService.KEY_LAST);
    } else {
      this.saveLast(llm);
    }
  }


  public async saveAsNew(): Promise<void> {
    const sgist = await this.savedFlowClient.create(this.modelobs.getModel());
    this.saveLast({
      id: sgist.uid,
      source: LoadSource.terminal
    });
  }

  public async updateRemote(): Promise<void> {
    const llm = this.getLastMark();
    if (!llm) { throw new Error('not find last save'); }
    await this.savedFlowClient.save(this.modelobs.getModel());
  }

  public async registerRemote(): Promise<void> {
    if (!this.modelobs.getModel()) { throw new Error('null the model'); }
    await this.flowClient.registerFlowByUid(this.modelobs.getModel().uid).toPromise();
  }

  public async testRemote(): Promise<void> {
    if (!this.modelobs.getModel()) { throw new Error('null the model'); }
    await this.flowClient.testFlow(this.modelobs.getModel().uid).toPromise();
  }



  private saveLast(iid: LoadLastMark): void {
    const llm = JSON.stringify(iid);
    localStorage.setItem(ModifyingFlowService.KEY_LAST, llm);
  }

}

export enum LoadSource {
  terminal = 'terminal'
}

export class LoadLastMark {
  public source: LoadSource;
  public id: string;
}
