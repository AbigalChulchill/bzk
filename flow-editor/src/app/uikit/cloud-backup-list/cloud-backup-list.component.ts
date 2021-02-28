import { Flow } from './../../model/flow';
import { JobClientService } from './../../service/job-client.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Gist, GistFile } from 'src/app/dto/gist';
import { GithubService } from 'src/app/service/github.service';
import { HttpClientService } from 'src/app/service/http-client.service';
import { LoadingService } from 'src/app/service/loading.service';
import { LoadSource, ModifyingFlowService } from 'src/app/service/modifying-flow.service';
import { BzkUtils } from 'src/app/utils/bzk-utils';

@Component({
  selector: 'app-cloud-backup-list',
  templateUrl: './cloud-backup-list.component.html',
  styleUrls: ['./cloud-backup-list.component.css']
})
export class CloudBackupListComponent implements OnInit {


  public devGitsJson: string;
  public bzkGists = new Array<Row>();
  public onImportDoneAction = () => { };

  constructor(
    public githubService: GithubService,
    private httpClient: HttpClientService,
    private router: Router,
    private modifyingFlow: ModifyingFlowService,
    private loading: LoadingService,
    private jobClient: JobClientService
  ) { }

  async ngOnInit(): Promise<void> {
    if (!this.githubService.hasAuth()) {
      this.githubService.postAuth(this.router.url);
      return;
    }
    this.reflesh();
  }

  private async reflesh(): Promise<void> {
    const gds = await this.githubService.listBzkGits();
    this.devGitsJson = JSON.stringify(gds, null, 4);
    this.bzkGists = new Array<Row>();
    for (const g of gds) {
      const e = this.genRow(g);
      this.bzkGists.push(e);

    }

  }

  private genRow(g: Gist): Row {
    const ans = new Row();
    ans.setContent(g.getMainFile().content);
    ans.createdAt = g.created_at;
    ans.name = ans.getFlow().name;
    ans.updateAt = g.updated_at;
    ans.cuid = g.id;
    return ans;
  }

  public async deleteGist(id: string): Promise<void> {
    const l = this.loading.show();
    await this.httpClient.deleteGist(id).toPromise();
    this.loading.dismiss(l);
  }

  public postGitHubAuth(): void {
    // alert(this.router.url);
    this.githubService.postAuth(this.router.url);
  }

  public async onImport(gr: Row): Promise<void> {

    const t = this.loading.show();
    this.import(gr);
    await this.onImportDoneAction();
    this.loading.dismiss(t);
  }

  private async import(gr: Row): Promise<void> {
    const fm = gr.getFlow();
    const sf = await this.jobClient.save(fm).toPromise();
  }

  public selectAll(): void {
    this.bzkGists.forEach(b => b.selected = true);
  }

  public unselectAll(): void {
    this.bzkGists.forEach(b => b.selected = false);
  }

  public hasSelected(): boolean {
    return this.bzkGists.filter(b => b.selected).length > 0;
  }

  public async importSelect(): Promise<void> {
    const t = this.loading.show();
    const sl = this.bzkGists.filter(b => b.selected);
    for (const sb of sl) {
      await this.import(sb);
    }
    await this.onImportDoneAction();
    this.loading.dismiss(t);
  }

}

export class Row {
  public cuid: string; //ungue cloud id
  private content: string;
  public name:string;
  public selected: boolean;
  public createdAt: Date;
  public updateAt: Date;

  public setContent(f: string):void {
    this.content = f;
  }

  public getFlow():Flow{
    const o = JSON.parse(this.content);
    const ans = BzkUtils.fitClzz(Flow, o);
    console.log(ans);
    return ans;
  }






}
