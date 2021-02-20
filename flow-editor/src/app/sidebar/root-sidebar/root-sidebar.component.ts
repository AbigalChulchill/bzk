import { Component, OnInit } from '@angular/core';
import { UrlParamsService } from 'src/app/service/url-params.service';

@Component({
  selector: 'app-root-sidebar',
  templateUrl: './root-sidebar.component.html',
  styleUrls: ['./root-sidebar.component.css']
})
export class RootSidebarComponent implements OnInit {

  constructor(
    public urlParam: UrlParamsService
  ) { }

  ngOnInit(): void {
  }

}
