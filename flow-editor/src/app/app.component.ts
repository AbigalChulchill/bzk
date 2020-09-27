import { Flow } from 'src/app/model/flow';
import { LanguageService } from './service/language.service';
import { Component, OnInit } from '@angular/core';
import { UrlParamsService } from './service/url-params.service';
import { GithubService } from './service/github.service';
import { PropUtils } from './utils/prop-utils';

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
    public language: LanguageService
  ) {
    PropUtils.init(language);
  }
  ngOnInit(): void {


    // const f = new Flow();


  }

}
