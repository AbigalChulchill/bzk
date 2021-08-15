import { RunLogComponent } from './run-log/run-log.component';
import { JobComponent } from './jobs/job/job.component';
import { JobsComponent } from './jobs/jobs.component';
import { LoginComponent } from './login/login.component';
import { ConfigComponent } from './config/config.component';
import { ConsoleComponent } from './console/console.component';
import { PathGuideService } from './service/path-guide.service';
import { FlowDesignComponent } from './flow-design/flow-design.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RootSidebarComponent } from './sidebar/root-sidebar/root-sidebar.component';
import { JobSidebarComponent } from './jobs/job/job-sidebar/job-sidebar.component';
import { VarCfgComponent } from './var-cfg/var-cfg.component';

// https://www.techiediaries.com/angular-router-multiple-outlets/ NOT WORK
const routes: Routes = [
  { path: '', component: FlowDesignComponent , canActivate: [PathGuideService] },
  { path: 'console', component: ConsoleComponent , canActivate: [PathGuideService] },
  { path: 'varcfg', component: VarCfgComponent , canActivate: [PathGuideService] },
  { path: 'model/design', component: FlowDesignComponent , canActivate: [PathGuideService] },
  { path: 'model/jobs', component: JobsComponent, canActivate: [PathGuideService] },
  { path: 'job/:uid',   component: JobComponent, canActivate: [PathGuideService] },
  { path: 'job/:uid', component: JobSidebarComponent, outlet:'sidebar' },
  { path: 'job/:uid/log/:queryUid', component: RunLogComponent,canActivate: [PathGuideService]  },
  { path: 'config', component: ConfigComponent, canActivate: [PathGuideService] },
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LoginComponent, canActivate: [PathGuideService]  },


  { path: '', component: RootSidebarComponent , outlet:'sidebar' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
