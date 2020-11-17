import { ReflectUtils } from './utils/reflect-utils';
import { CommUtils } from './utils/comm-utils';
import { OType } from './model/bzk-obj';
import { BzkUtils } from './utils/bzk-utils';
import { Flow } from 'src/app/model/flow';
import { LanguageService } from './service/language.service';
import { Component, OnInit } from '@angular/core';
import { UrlParamsService } from './service/url-params.service';
import { GithubService } from './service/github.service';
import { PropUtils } from './utils/prop-utils';
import { LoadingService } from './service/loading.service';
import { Router, NavigationEnd } from '@angular/router';
import { Entry } from './model/entry';
import { Action, NodejsAction } from './model/action';
import { ClassType } from 'class-transformer/ClassTransformer';
import { VarStoreComponent } from './dto/view/var-store/var-store.component';
import { FlowComponent } from './model/view/flow/flow.component';
import { BoxComponent } from './model/view/box/box.component';
import { ActionComponent } from './model/view/action/action.component';
import { LinkComponent } from './model/view/link/link.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'flow-editor';

  constructor(
    public urlParams: UrlParamsService,
    public githubService: GithubService,
    public language: LanguageService,
    private loading: LoadingService,
    router: Router
  ) {
    PropUtils.init(language);
    router.events.forEach((event) => {
      if (event instanceof NavigationEnd) {
        console.log('NavigationEnd:' + event);
        this.loading.twinkle();
      }
    });
  }


  ngOnInit(): void {
    BzkUtils.activity(Entry);
    const na = new NodejsAction();
    const nafc = ReflectUtils.listParents(NodejsAction);
    console.log('nafc:' + nafc);
    const cclzs = BzkUtils.listChilds(Action);
    console.log('cclzs:' + cclzs);


    PropUtils.getInstance().bindExView('VarStoreComponent', VarStoreComponent);
    PropUtils.getInstance().bindExView('FlowComponent', FlowComponent);
    PropUtils.getInstance().bindExView('BoxComponent', BoxComponent);
    PropUtils.getInstance().bindExView('ActionComponent', ActionComponent);
    PropUtils.getInstance().bindExView('LinkComponent', LinkComponent);



  }

}
