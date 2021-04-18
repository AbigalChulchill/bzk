import { BasicAuthHtppInterceptorService } from './service/basic-auth-htpp-interceptor.service';
import { GithubService } from './service/github.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FlowDesignComponent } from './flow-design/flow-design.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { PropertiesComponent } from './flow-design/properties/properties.component';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSelectModule } from '@angular/material/select';
import { MatExpansionModule } from '@angular/material/expansion';
import { PropRowComponent } from './flow-design/properties/prop-row/prop-row.component';
import { CustomDirective } from './flow-design/properties/custom.directive';
import { BoxComponent } from './model/view/box/box.component';
import { ActionComponent } from './model/view/action/action.component';
import { FlowComponent } from './model/view/flow/flow.component';
import { LinkComponent } from './model/view/link/link.component';
import { TaskFuncViewComponent } from './model/view/task-func-view/task-func-view.component';
import { JsonEditorComponent } from './uikit/json-editor/json-editor.component';
import { VarStoreComponent } from './dto/view/var-store/var-store.component';
import { ConsoleComponent } from './console/console.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { BoxRunLogComponent } from './console/box-run-log/box-run-log.component';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ConditionComponent } from './model/view/link/condition/condition.component';
import { ConfigComponent } from './config/config.component';
import { ConditionNumComponent } from './model/view/link/condition-num/condition-num.component';
import { ConditionEnumeratorComponent } from './model/view/link/condition-enumerator/condition-enumerator.component';
import { ConditionTxtComponent } from './model/view/link/condition-txt/condition-txt.component';
import { ConditionIncludeComponent } from './model/view/link/condition-include/condition-include.component';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { PropListComponent } from './flow-design/properties/prop-row/prop-list/prop-list.component';
import { PropMapComponent } from './flow-design/properties/prop-row/prop-map/prop-map.component';
import { HttpActionComponent } from './model/view/action/http-action/http-action.component';
import { MatDialogModule } from '@angular/material/dialog';
import { RefTextComponent } from './uikit/ref-text/ref-text.component';
import { PolyglotActionComponent } from './model/view/action/polyglot-action/polyglot-action.component';
import { KVPairComponent } from './uikit/kvpair/kvpair.component';
import { MonacoEditorModule } from 'ngx-monaco-editor';
import { CodeEditorComponent } from './uikit/code-editor/code-editor.component';
import { VarKeyComponent } from './uikit/var-key/var-key.component';
import { SubFlowActionComponent } from './model/view/action/sub-flow-action/sub-flow-action.component';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { TransitionComponent } from './model/view/transition/transition.component';
import { VarKeyReflectComponent } from './uikit/var-key-reflect/var-key-reflect.component';
import { LoginComponent } from './login/login.component';
import { CloudBackupListComponent } from './uikit/cloud-backup-list/cloud-backup-list.component';
import { JobsComponent } from './jobs/jobs.component';
import { JobComponent } from './jobs/job/job.component';
import { RootSidebarComponent } from './sidebar/root-sidebar/root-sidebar.component';
import { JobSidebarComponent } from './jobs/job/job-sidebar/job-sidebar.component';
import { RunLogComponent } from './run-log/run-log.component';
import { LogRowComponent } from './run-log/log-row/log-row.component';
import { VarCfgComponent } from './var-cfg/var-cfg.component';
import {MatProgressBarModule} from '@angular/material/progress-bar';

export function createTranslateLoader(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    FlowDesignComponent,
    SidebarComponent,
    PropertiesComponent,
    PropRowComponent,
    CustomDirective,
    BoxComponent,
    ActionComponent,
    FlowComponent,
    LinkComponent,
    TaskFuncViewComponent,
    JsonEditorComponent,
    VarStoreComponent,
    ConsoleComponent,
    BoxRunLogComponent,
    ConditionComponent,
    ConfigComponent,
    ConditionNumComponent,
    ConditionEnumeratorComponent,
    ConditionTxtComponent,
    ConditionIncludeComponent,
    PropListComponent,
    PropMapComponent,
    HttpActionComponent,
    RefTextComponent,
    PolyglotActionComponent,
    KVPairComponent,
    CodeEditorComponent,
    VarKeyComponent,
    SubFlowActionComponent,
    TransitionComponent,
    VarKeyReflectComponent,
    LoginComponent,
    CloudBackupListComponent,
    JobsComponent,
    JobComponent,
    RootSidebarComponent,
    JobSidebarComponent,
    RunLogComponent,
    LogRowComponent,
    VarCfgComponent,

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    TranslateModule,
    MatSelectModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatExpansionModule,
    MatSortModule,
    MatTableModule,
    ClipboardModule,
    MatDialogModule,
    MatProgressBarModule,
    MatPaginatorModule,
    MonacoEditorModule.forRoot(),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    }),
    BrowserAnimationsModule,
    MatSlideToggleModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS, useClass: GithubService, multi: true
    },
    {
      provide: HTTP_INTERCEPTORS, useClass: BasicAuthHtppInterceptorService, multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
