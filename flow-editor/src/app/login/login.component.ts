import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username = 'domidev';
  password = 'password';
  invalidLogin = false;

  constructor(
    private router: Router,
  ) { }

  ngOnInit() {
  }

  checkLogin() {


  }

  public getGoogleOauthUrl(): string {
    return environment.apiHost + '/oauth2/authorize/google?redirect_uri=' + window.location.origin + '/oauth2/redirect';
  }

}
