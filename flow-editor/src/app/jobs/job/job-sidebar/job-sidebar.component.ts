import { async } from '@angular/core/testing';
import { LoadingService } from './../../../service/loading.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FlowPoolInfo } from 'src/app/dto/flow-pool-info';
import { FlowClientService } from 'src/app/service/flow-client.service';
import { UrlParamsService } from 'src/app/service/url-params.service';
import { ModifyingFlowService } from 'src/app/service/modifying-flow.service';
import { JobRunInfo } from 'src/app/dto/job-dto';
import { JobClientService } from 'src/app/service/job-client.service';

@Component({
  selector: 'app-job-sidebar',
  templateUrl: './job-sidebar.component.html',
  styleUrls: ['./job-sidebar.component.css']
})
export class JobSidebarComponent implements OnInit {

  public uid = '';
  public jobRunInfo=new  JobRunInfo();

  constructor(
    public urlParam: UrlParamsService,
    private route: ActivatedRoute,
    private flowClient: FlowClientService,
    private jobClient: JobClientService,
    private loading: LoadingService,
    public modifyingFlow: ModifyingFlowService,
  ) { }

  async ngOnInit(): Promise<void> {
    this.uid = this.route.snapshot.paramMap.get('uid');
    await this.reflesh();
  }

  private async reflesh(a = () => { }): Promise<void> {
    const t = this.loading.show();
    await a();
    try {
      this.jobRunInfo = await  this.jobClient.getInfo(this.uid);
    } catch (e) {

    }
    this.loading.dismiss(t);
  }

  public isEnable(): boolean {
    return this.jobRunInfo.enable;
  }

  public runManual(): void {

    this.reflesh(async () => {
      await this.flowClient.runManual(this.uid).toPromise();
    });
  }


  public toggleEnable(): void {
    if (this.isEnable()) {
      this.disableJob();
    } else {
      this.enableJob();
    }
  }

  public async disableJob(): Promise<void> {
    await this.reflesh(async () => {
      await this.flowClient.forceRemovePool(this.uid).toPromise();
    });
  }

  public async enableJob(): Promise<void> {
    await this.reflesh(async () => {
      await this.flowClient.registerFlowByUid(this.uid).toPromise();
    });
  }

}
