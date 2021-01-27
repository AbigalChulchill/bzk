import { CommUtils } from './../utils/comm-utils';
import { VarsStore } from './../dto/vars-store';

import { HttpClientService } from './http-client.service';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UrlParamsService } from './url-params.service';
import { Router, NavigationEnd } from '@angular/router';
import { StringUtils } from '../utils/string-utils';
import { Flow } from '../model/flow';
import { Gist, GistFile } from '../dto/gist';
import { Constant } from '../infrastructure/constant';

@Injectable({
  providedIn: 'root'
})
export class GithubService implements HttpInterceptor {


  public static KEY_TOKE = 'GitHubToken';
  public static KEY_PAGE = 'GitHubPostPage';
  public static KEY_ENCRYPTION = 'GitHub_ENCRYPTION_PASS';

  public encryptionPass = '';

  constructor(
    private urlParams: UrlParamsService,
    private router: Router,
    private httpClient: HttpClientService
  ) {
    router.events.forEach((event) => {
      if (event instanceof NavigationEnd) {
        console.log('NavigationEnd:' + event);
        if (urlParams.hasGitToken()) {
          this.postAuthTask();
        }
      }
    });
    this.encryptionPass = localStorage.getItem(GithubService.KEY_ENCRYPTION);
  }



  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('intercept:' + req);
    if (req.url.startsWith(environment.githubHost)) {
      const ht = 'Bearer ' + this.getAuthToken();
      req = req.clone({
        setHeaders: {
          Authorization: ht,
          accept: 'application/vnd.github.v3+json'
        }
      });
    }
    return next.handle(req);
  }

  private postAuthTask(): void {
    const tk = this.urlParams.getGitToken();
    sessionStorage.setItem(GithubService.KEY_TOKE, tk);
    this.router.navigate([sessionStorage.getItem(GithubService.KEY_PAGE)]);
  }

  public hasAuth(): boolean {
    return !StringUtils.isBlank(sessionStorage.getItem(GithubService.KEY_TOKE));
  }

  public getAuthToken(): string {
    return sessionStorage.getItem(GithubService.KEY_TOKE);
  }

  public genAuthLink(): string {
    return 'https://github.com/login/oauth/authorize?client_id=3630861d7b003196f9b1&scope=user%20public_repo%20gist';
  }

  public postAuth(page: string): void {
    sessionStorage.setItem(GithubService.KEY_PAGE, page);
    window.location.href = this.genAuthLink();
  }

  public async listGits(): Promise<Array<Gist>> {
    return this.httpClient.listGits();
  }

  public async listBzkGits(): Promise<Array<Gist>> {
    const gits = await this.listGits();
    const fs = gits.filter(g => {
      return g.getMainFile();
    });
    const ans = new Array<Gist>();
    for (const g of fs) {
      try {
        ans.push(await this.getGist(g.id));
      } catch (ex) {
        console.warn(ex);
      }
    }
    return ans;
  }

  public getGist(id: string): Promise<Gist> {
    return this.httpClient.getGist(id, this.encryptionPass);
  }


  public async createModel(inf: Flow, vs: VarsStore): Promise<Gist> {
    const f:Flow = CommUtils.clone(inf);
    f.uid =  CommUtils.makeAlphanumeric(Constant.UID_SIZE);
    const fo = await this.httpClient.createGits(false, this.encryptionPass, 'it`s Bzk', this.genFiles(f, vs));
    console.log('f:' + console.log(fo));
    return fo;
  }

  public async updateModel(id: string, f: Flow, vs: VarsStore): Promise<Gist> {
    const fo = await this.httpClient.updateGist(id, this.encryptionPass, this.genFiles(f, vs));
    return fo;
  }

  public genFiles(f: Flow, vs: VarsStore): Array<GistFile> {
    const gfs = new Array<GistFile>();
    const gf = new GistFile();
    gf.filename = f.uid + Gist.KEY_MAIN_GIST_EXTENSION;
    gf.content = JSON.stringify(f);
    gfs.push(gf);

    const vsf = new GistFile();
    vsf.filename = Constant.VARS_STORE_NAME;
    vsf.content = JSON.stringify(vs);
    gfs.push(vsf);

    return gfs;
  }

}
