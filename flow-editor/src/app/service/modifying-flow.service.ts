import { GithubService } from './github.service';
import { Flow } from './../model/flow';
import { Injectable } from '@angular/core';
import { HttpClientService } from './http-client.service';
import { plainToClass } from 'class-transformer';
import { PropUtils } from '../utils/prop-utils';

@Injectable({
  providedIn: 'root'
})
export class ModifyingFlowService {


  public model: Flow;

  constructor(
    private clientService: HttpClientService,
    private githubService: GithubService
  ) { }

  public async loadDemo(): Promise<void> {
    const lm = await this.clientService.getDemoModel().toPromise();
    this.model = plainToClass(Flow, lm);
  }

  public setTarget(f: Flow): void {
    this.model = plainToClass(Flow, f);
  }


  public saveAsNew(): void {
    this.githubService.pushModel(this.model);
  }

}

export class LoadLastMark {



}
