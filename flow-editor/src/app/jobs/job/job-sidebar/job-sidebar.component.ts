import { Component, OnInit } from '@angular/core';
import { UrlParamsService } from 'src/app/service/url-params.service';

@Component({
  selector: 'app-job-sidebar',
  templateUrl: './job-sidebar.component.html',
  styleUrls: ['./job-sidebar.component.css']
})
export class JobSidebarComponent implements OnInit {

  constructor(
    public urlParam:UrlParamsService
  ) { }

  ngOnInit(): void {
  }

}
