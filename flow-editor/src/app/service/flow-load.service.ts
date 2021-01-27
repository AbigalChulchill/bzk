import { Injectable } from '@angular/core';
import { Gist } from '../dto/gist';
import { Flow } from '../model/flow';
import { GithubService } from './github.service';



export class FlowRaw {
  public id: string;
  public name: string;
  public actions: string;

  public getModel(): Flow { throw new Error('not support!'); }

}

export class GistFlowRaw extends FlowRaw {


  public gist: Gist
  public model: Flow;
  public constructor(g: Gist) {
    super();
    this.model = g.getMainFile().convertModel();
    this.id = this.model.uid;
    this.name = this.model.name;
  }

  public getModel(): Flow { return this.model; }

}

@Injectable({
  providedIn: 'root'
})
export class FlowLoadService {
  private static instance: FlowLoadService;
  private raws : Array<FlowRaw>;
  constructor(
    public githubService: GithubService,
  ) {
    FlowLoadService.instance = this;
  }

  private async initLoad(): Promise<Array<FlowRaw>> {
    if(this.raws){
      return this.raws;
    }
    const gs = await this.githubService.listBzkGits();
    this.raws = new Array<FlowRaw>();
    for (const g of gs) {
      this.raws.push(new GistFlowRaw(g));
    }
    return this.raws;
  }

  public async getRaws(): Promise<Array<FlowRaw>> {
    return await this.initLoad();
  }

  public async find(uid:string): Promise< FlowRaw>{
    await this.initLoad();
    return this.raws.find(r=> r.getModel().uid === uid);
  }

  public static getInstance(): FlowLoadService {
    return FlowLoadService.instance;
  }
}
