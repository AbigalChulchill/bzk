import { Flow } from './../model/flow';
import { LoadSource, ModifyingFlowService } from './../service/modifying-flow.service';
import { GithubService } from './../service/github.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Gist, GistFile } from '../dto/gist';

@Component({
  selector: 'app-model-repo',
  templateUrl: './model-repo.component.html',
  styleUrls: ['./model-repo.component.css']
})
export class ModelRepoComponent implements OnInit {


  public devGitsJson: string;
  public bzkGists = new Array<GistRow>();

  constructor(
    public githubService: GithubService,
    private router: Router,
    private modifyingFlow: ModifyingFlowService,
  ) { }

  async ngOnInit(): Promise<void> {
    if (!this.githubService.hasAuth()) {
      this.githubService.postAuth(this.router.url);
      return;
    }
    this.reflesh();
  }

  private async reflesh(): Promise<void> {
    this.bzkGists = null;
    const gds = await this.githubService.listBzkGits();
    this.devGitsJson = JSON.stringify(gds, null, 4);
    this.bzkGists = new Array<GistRow>();
    for (const g of gds) {
      this.bzkGists.push(new GistRow(g));
    }
  }

  public postGitHubAuth(): void {
    alert(this.router.url);
    this.githubService.postAuth(this.router.url);
  }

  public async onFileEdit(gr: GistRow): Promise<void> {
    this.modifyingFlow.setTarget(gr.gist.getMainFile().convertModel(), {
      id: gr.gist.id,
      source: LoadSource.Gist
    });
    this.router.navigate(['model/design']);
  }



}

export class GistRow {

  public gist: Gist;
  public selected: boolean;

  constructor(g: Gist) {
    this.gist = g;
  }

  public getMainFile(): GistFile {
    return this.gist.getMainFile();
  }

  public get name(): string {
    return this.getMainFile().convertModel().name;
  }



}
