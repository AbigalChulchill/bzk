import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { AuthenticationService } from './authentication.service';
import { ToastService } from './toast.service';

@Injectable({
  providedIn: 'root'
})
export class PathGuideService implements CanActivate {

  constructor(
    private router: Router,
    private toast: ToastService,
    private language: TranslateService,
    private authentication: AuthenticationService
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

    if(!this.authentication.isUserLoggedIn()){
      this.router.navigate(['login']);
      return false;
    }

    if (state.url === '/') {
      this.router.navigate(['model/design']);
      return false;
    }

    if (state.url === '/logout') {
      this.authentication.logOut();
      this.router.navigate(['login']);
      return false;
    }


    return true;

  }
}
