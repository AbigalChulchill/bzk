import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { skip } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BasicAuthHtppInterceptorService implements HttpInterceptor {

  constructor() { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {

    if (this.isSkip(req)){
      return next.handle(req);
    }

    if (localStorage.getItem('token') && !this.isSkipTokenByUrl(req)) {
      req = req.clone({
        setHeaders: {
          Authorization: localStorage.getItem('token')
        }
      });
    }

    req = req.clone({
      setHeaders: {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Credentials': 'true',
        'Access-Control-Allow-Headers': 'Access-Control-Allow-Methods,Access-Control-Allow-Credentials,Access-Control-Allow-Origin,Access-Control-Allow-Headers,Authorization,Accept,Origin,DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range',
        'Access-Control-Allow-Methods': 'GET, PUT, POST, DELETE, OPTIONS, PATCH',
        'Access-Control-Max-Age': '86400'
      }
    });

    return next.handle(req);

  }

  private isSkip(req: HttpRequest<any>): boolean {
    const url = new URL(req.url);
    const path = url.pathname;

    if (req.url.startsWith(environment.githubHost)) return true;
    return false;
  }

  private isSkipTokenByUrl(req: HttpRequest<any>): boolean {
    const url = new URL(req.url);
    const path = url.pathname;
    console.log('path:' + path);

    if (url.origin + '/' === environment.githubHost) return true;

    if (path.startsWith('/authenticate')) { return true; }
    if (path.startsWith('/register')) { return true; }
    return false;
  }
}
