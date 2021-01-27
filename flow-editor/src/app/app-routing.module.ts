import { ConfigComponent } from './config/config.component';
import { ConsoleComponent } from './console/console.component';
import { RegisteredFlowComponent } from './registered-flow/registered-flow.component';
import { PathGuideService } from './service/path-guide.service';
import { ModelRepoComponent } from './model-repo/model-repo.component';
import { FlowDesignComponent } from './flow-design/flow-design.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// https://www.techiediaries.com/angular-router-multiple-outlets/ NOT WORK
const routes: Routes = [
  { path: '', component: FlowDesignComponent , canActivate: [PathGuideService] },
  { path: 'console', component: ConsoleComponent , canActivate: [PathGuideService] },
  { path: 'model/design', component: FlowDesignComponent , canActivate: [PathGuideService] },
  { path: 'model/repo', component: ModelRepoComponent, canActivate: [PathGuideService] },
  { path: 'model/registered', component: RegisteredFlowComponent, canActivate: [PathGuideService] },
  { path: 'config', component: ConfigComponent, canActivate: [PathGuideService] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
