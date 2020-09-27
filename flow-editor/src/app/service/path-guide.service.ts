import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { ToastService } from './toast.service';

@Injectable({
  providedIn: 'root'
})
export class PathGuideService implements CanActivate {

  constructor(
    private router: Router,
    private toast: ToastService,
    private language: TranslateService,
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

    if (state.url === '/') {
      this.router.navigate(['model/design']);
      return false;
    }


    return true;

  }
}
