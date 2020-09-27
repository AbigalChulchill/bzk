import { Flow } from './../model/flow';
import { ModifyingFlowService } from './../service/modifying-flow.service';
import { GithubService } from './../service/github.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Gist, GistFile } from '../service/http-client.service';

@Component({
  selector: 'app-model-repo',
  templateUrl: './model-repo.component.html',
  styleUrls: ['./model-repo.component.css']
})
export class ModelRepoComponent implements OnInit {


  public devGitsJson: string;
  public bzkGists = new Array<GistRow>();;

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
    const lg = await this.githubService.getGist(gr.gist.id).toPromise();
    const gmf = GithubService.getMainFile(lg);
    const f: Flow = JSON.parse(gmf.content);

    this.modifyingFlow.setTarget(f);
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
    return GithubService.getMainFile(this.gist);
  }

  public get name(): string {
    return GithubService.getMainFile(this.gist).filename.replace(GithubService.KEY_MAIN_GIST_EXTENSION, '');
  }



}
