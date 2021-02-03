import { SavedFlowClientService } from './../../service/saved-flow-client.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Gist, GistFile } from 'src/app/dto/gist';
import { GithubService } from 'src/app/service/github.service';
import { HttpClientService } from 'src/app/service/http-client.service';
import { LoadingService } from 'src/app/service/loading.service';
import { LoadSource, ModifyingFlowService } from 'src/app/service/modifying-flow.service';

@Component({
  selector: 'app-cloud-backup-list',
  templateUrl: './cloud-backup-list.component.html',
  styleUrls: ['./cloud-backup-list.component.css']
})
export class CloudBackupListComponent implements OnInit {


  public devGitsJson: string;
  public bzkGists = new Array<GistRow>();
  public onImportDoneAction = () => {};

  constructor(
    public githubService: GithubService,
    private httpClient: HttpClientService,
    private router: Router,
    private modifyingFlow: ModifyingFlowService,
    private loading: LoadingService,
    private savedFlowClient: SavedFlowClientService
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

  public async deleteGist(id: string): Promise<void> {
    const l = this.loading.show();
    await this.httpClient.deleteGist(id).toPromise();
    this.loading.dismiss(l);
  }

  public postGitHubAuth(): void {
    alert(this.router.url);
    this.githubService.postAuth(this.router.url);
  }

  public async onImport(gr: GistRow): Promise<void> {

    const t = this.loading.show();
    const fm = gr.gist.getMainFile().convertModel();
    const sf = await this.savedFlowClient.save(fm).toPromise();
    await this.onImportDoneAction();
    this.loading.dismiss(t);

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
