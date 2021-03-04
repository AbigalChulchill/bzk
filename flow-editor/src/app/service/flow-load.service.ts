import { Job } from './../model/job';
import { Injectable } from '@angular/core';
import { Gist } from '../dto/gist';
import { Flow } from '../model/flow';
import { GithubService } from './github.service';
import { JobClientService } from './job-client.service';



export class FlowRaw {
  public id: string;
  public name: string;
  public actions: string;
  private job:Job;

  public constructor(j:Job){
    this.job = j;
    this.id = j.uid;
    this.name = j.model.name;
  }

  public getModel(): Flow { return this.job.model; }

}

@Injectable({
  providedIn: 'root'
})
export class FlowLoadService {
  private static instance: FlowLoadService;
  private raws : Array<FlowRaw>;
  constructor(
    public jobClient: JobClientService,
  ) {
    FlowLoadService.instance = this;
  }

  private async initLoad(): Promise<Array<FlowRaw>> {
    if(this.raws){
      return this.raws;
    }
    const gs = await this.jobClient.listAll();
    this.raws = new Array<FlowRaw>();
    for (const g of gs) {
      this.raws.push(new FlowRaw(g));
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
