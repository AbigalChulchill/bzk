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
  public varsStore = new VarsStore();
  public gists = new Array<Gist>();

  constructor(
    private clientService: HttpClientService,
    private flowClient:FlowClientService,
    private githubService: GithubService,
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
    this.gists = await this.githubService.listBzkGits();
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
      const gist = await this.githubService.getGist(llm.id);
      this.modelobs.setModel(gist.getMainFile().convertModel());
      this.varsStore = gist.getVarsStore();
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
    const sgist = await this.githubService.createModel(this.modelobs.getModel(), this.varsStore);
    this.saveLast({
      id: sgist.id,
      source: LoadSource.Gist
    });
  }

  public async updateRemote(): Promise<void> {
    const llm = this.getLastMark();
    if (!llm) { throw new Error('not find last save'); }
    await this.githubService.updateModel(llm.id, this.modelobs.getModel(), this.varsStore);
  }

  public async registerRemote(): Promise<void> {
    if (!this.modelobs.getModel()) { throw new Error('null the model'); }
    const allfs = new Array<Flow>();
    this.listAllDependsFlow(allfs, this.modelobs.getModel());
    await this.flowClient.registerFlow(allfs).toPromise();
  }

  public async testRemote(): Promise<void> {
    if (!this.modelobs.getModel()) { throw new Error('null the model'); }
    const allfs = new Array<Flow>();
    this.listAllDependsFlow(allfs, this.modelobs.getModel());
    await this.flowClient.testFlow(this.modelobs.getModel().uid,allfs).toPromise();
  }

  public listAllDependsFlow(ans: Array<Flow>, f: Flow) {
    if (!ModelUtils.pushUnique(ans, f)) return;
    const sfas = ModelUtils.listAllAction(f).filter(a => a instanceof SubFlowAction);
    for (const nsfa of sfas) {
      const sfa: SubFlowAction = nsfa as SubFlowAction;
      const g = this.gists.find(g => g.getMainFile().convertModel().uid === sfa.flowUid);
      const df = g.getMainFile().convertModel();
      if (!ModelUtils.pushUnique(ans, df)) continue;
      this.listAllDependsFlow(ans, df);
    }
  }

  private saveLast(iid: LoadLastMark): void {
    const llm = JSON.stringify(iid);
    localStorage.setItem(ModifyingFlowService.KEY_LAST, llm);
  }

}

export enum LoadSource {
  Gist = 'Gist'
}

export class LoadLastMark {
  public source: LoadSource;
  public id: string;
}
