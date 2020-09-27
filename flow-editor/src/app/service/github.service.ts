import { Gist, GistFile, HttpClientService } from './http-client.service';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UrlParamsService } from './url-params.service';
import { Router, NavigationEnd } from '@angular/router';
import { StringUtils } from '../utils/string-utils';
import { Flow } from '../model/flow';

@Injectable({
  providedIn: 'root'
})
export class GithubService implements HttpInterceptor {

  public static KEY_MAIN_GIST_EXTENSION = '.bzk';
  public static KEY_TOKE = 'GitHubToken';
  public static KEY_PAGE = 'GitHubPostPage';

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

  }

  public static getMainFile(g: Gist): GistFile {
    const ks = Object.keys(g.files);
    for (const k of ks) {
      if (k.endsWith(GithubService.KEY_MAIN_GIST_EXTENSION)) { return g.files[k]; }
    }
    return null;
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

  public listGits(): Observable<Array<Gist>> {
    return this.httpClient.listGits();
  }

  public async listBzkGits(): Promise<Array<Gist>> {
    const gits = await this.listGits().toPromise();
    const ans = gits.filter(g => {
      return GithubService.getMainFile(g);
    });
    return ans;
  }

  public getGist(id: string): Observable<Gist> {
    return this.httpClient.getGist(id);
  }


  public async pushModel(f: Flow): Promise<void> {

    const gfs = new Array<GistFile>();
    gfs.push({
      filename: f.name + GithubService.KEY_MAIN_GIST_EXTENSION,
      content: JSON.stringify(f, null, 4)
    });
    const fo = await this.httpClient.createGits(false, 'it`s Bzk', gfs).toPromise();
    console.log('f:' + console.log(fo));
  }

}
