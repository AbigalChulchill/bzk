import { Injectable } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute, ParamMap } from '@angular/router';
import { ListLogType } from './run-log-client.service';

@Injectable({
  providedIn: 'root'
})
export class UrlParamsService {

  public path: string;
  private route:ActivatedRoute;

  constructor(
    private router: Router
  ) {

    router.events.forEach((event) => {
      if (event instanceof NavigationEnd) {
        console.log('NavigationEnd:' + event);
        this.path = event.url;
        console.log('this.path:' + this.path);
        this.route = router.routerState.root.firstChild;
      }
    });

  }

  public getPath(): string {
    return this.path;
  }

  private params(): ParamMap {
    return this.route.snapshot.queryParamMap;
  }

  private parse(k: string): string {
    const ans = this.params().get(k);
    return ans;
  }

  public eqPath(p: string): boolean {
    if (!this.path) { return false; }
    return this.path.startsWith(p);
  }

  public getGitToken(): string {
    return this.parse('token');
  }

  public hasGitToken(): boolean {
    return this.params().has('token');
  }

  public getQueryUid(): string {
    return this.route.snapshot.paramMap.get('queryUid');
  }

  public getListType(): ListLogType {
    const key = 'listType';
    if (this.route.snapshot.queryParamMap.has(key)) {
      return this.route.snapshot.queryParamMap.get(key) as ListLogType;
    }
    return ListLogType.runflow;
  }

  public genRunLogUrl(jonUid: string, queryUid: string, lt: ListLogType): string {
      return `/job/${jonUid}/log/${queryUid}?listType=${lt}`;
  }

}
