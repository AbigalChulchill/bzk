import { GithubService } from './service/github.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FlowDesignComponent } from './flow-design/flow-design.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { PropertiesComponent } from './flow-design/properties/properties.component';
import { ModelRepoComponent } from './model-repo/model-repo.component';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { FormsModule,ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatSelectModule} from '@angular/material/select';
import {MatExpansionModule} from '@angular/material/expansion';
import { PropRowComponent } from './flow-design/properties/prop-row/prop-row.component';
import { CustomDirective } from './flow-design/properties/custom.directive';
import { BoxComponent } from './model/view/box/box.component';
import { ActionComponent } from './model/view/action/action.component';
import { FlowComponent } from './model/view/flow/flow.component';
import { RegisteredFlowComponent } from './registered-flow/registered-flow.component';
import { LinkComponent } from './model/view/link/link.component';
import { TaskFuncViewComponent } from './model/view/task-func-view/task-func-view.component';
import { JsonEditorComponent } from './uikit/json-editor/json-editor.component';
import { VarStoreComponent } from './dto/view/var-store/var-store.component';
import { ConsoleComponent } from './console/console.component';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import { BoxRunLogComponent } from './console/box-run-log/box-run-log.component';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatFormFieldModule} from '@angular/material/form-field';


export function createTranslateLoader(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    FlowDesignComponent,
    SidebarComponent,
    PropertiesComponent,
    ModelRepoComponent,
    PropRowComponent,
    CustomDirective,
    BoxComponent,
    ActionComponent,
    FlowComponent,
    RegisteredFlowComponent,
    LinkComponent,
    TaskFuncViewComponent,
    JsonEditorComponent,
    VarStoreComponent,
    ConsoleComponent,
    BoxRunLogComponent,

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
    ReactiveFormsModule,
    MatExpansionModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    }),
    BrowserAnimationsModule,
    MatSlideToggleModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS, useClass: GithubService, multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
