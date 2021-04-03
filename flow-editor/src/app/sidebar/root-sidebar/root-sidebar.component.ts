import { async } from '@angular/core/testing';
import { JobClientService } from './../../service/job-client.service';
import { Component, OnInit } from '@angular/core';
import { UrlParamsService } from 'src/app/service/url-params.service';
import { LoadingService } from 'src/app/service/loading.service';
import { ModifyingFlowService } from 'src/app/service/modifying-flow.service';

@Component({
  selector: 'app-root-sidebar',
  templateUrl: './root-sidebar.component.html',
  styleUrls: ['./root-sidebar.component.css']
})
export class RootSidebarComponent implements OnInit {

  constructor(
    private loading: LoadingService,
    public urlParam: UrlParamsService,
    public jobClient:JobClientService,
    private modifyingFlow: ModifyingFlowService
  ) { }

  ngOnInit(): void {
  }

  public async newJob():Promise<void>{
    const t = this.loading.show();
    const gr = await this.jobClient.createNewOne().toPromise()
    this.modifyingFlow.goDesignPage(gr.model);
    this.loading.dismiss(t);
  }

}
