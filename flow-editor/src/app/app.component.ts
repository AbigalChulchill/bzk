import { VarKeyComponent } from './uikit/var-key/var-key.component';
import { PlaceholderUtils } from './utils/placeholder-utils';
import { plainToClass } from 'class-transformer';
import { ConditionIncludeComponent } from './model/view/link/condition-include/condition-include.component';
import { ReflectUtils } from './utils/reflect-utils';
import { BzkUtils } from './utils/bzk-utils';
import { LanguageService } from './service/language.service';
import { Component, OnInit } from '@angular/core';
import { UrlParamsService } from './service/url-params.service';
import { GithubService } from './service/github.service';
import { PropType, PropUtils } from './utils/prop-utils';
import { LoadingService } from './service/loading.service';
import { Router, NavigationEnd } from '@angular/router';
import { Entry } from './model/entry';
import { Action, NodejsAction } from './model/action';
import { VarStoreComponent } from './dto/view/var-store/var-store.component';
import { FlowComponent } from './model/view/flow/flow.component';
import { BoxComponent } from './model/view/box/box.component';
import { ActionComponent } from './model/view/action/action.component';
import { LinkComponent } from './model/view/link/link.component';
import { Condition } from './model/condition';
import { ConditionNumComponent } from './model/view/link/condition-num/condition-num.component';
import { ConditionTxtComponent } from './model/view/link/condition-txt/condition-txt.component';
import { JsonEditorComponent } from './uikit/json-editor/json-editor.component';
import { HttpActionComponent } from './model/view/action/http-action/http-action.component';
import { PolyglotActionComponent } from './model/view/action/polyglot-action/polyglot-action.component';
import { KVPairComponent } from './uikit/kvpair/kvpair.component';
import { SubFlowActionComponent } from './model/view/action/sub-flow-action/sub-flow-action.component';
import { VarService } from './service/var.service';
import { TransitionComponent } from './model/view/transition/transition.component';
import { ConditionEnumeratorComponent } from './model/view/link/condition-enumerator/condition-enumerator.component';
import { ConditionComponent } from './model/view/link/condition/condition.component';
import { VarKeyReflectComponent } from './uikit/var-key-reflect/var-key-reflect.component';

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
    private varService:VarService,
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
    BzkUtils.activity(Condition);
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
    PropUtils.getInstance().bindExView('ConditionNumComponent', ConditionNumComponent);
    PropUtils.getInstance().bindExView('ConditionTxtComponent', ConditionTxtComponent);
    PropUtils.getInstance().bindExView('ConditionIncludeComponent', ConditionIncludeComponent);
    PropUtils.getInstance().bindExView('JsonEditorComponent', JsonEditorComponent);
    PropUtils.getInstance().bindExView('HttpActionComponent', HttpActionComponent);
    PropUtils.getInstance().bindExView('PolyglotActionComponent', PolyglotActionComponent);
    PropUtils.getInstance().bindExView('KVPairComponent', KVPairComponent);
    PropUtils.getInstance().bindExView('VarKeyComponent', VarKeyComponent);
    PropUtils.getInstance().bindExView('SubFlowActionComponent', SubFlowActionComponent);
    PropUtils.getInstance().bindExView('TransitionComponent', TransitionComponent);
    PropUtils.getInstance().bindExView('ConditionComponent', ConditionComponent);
    PropUtils.getInstance().bindExView('VarKeyReflectComponent', VarKeyReflectComponent);


    PropUtils.getInstance().bindExAutoText('ListAllVarKeys',()=>this.varService.listAllKeys());



    // const testt = 'hi ${asf}  ${ddd} xx ${!dd.cc}';
    // console.log(testt);
    // const ps= PlaceholderUtils.listHolderKey('\\$\\{','\\}','[\\w . ! ~ $]',testt);
    // ps.forEach(p=> p. );
  }

}
