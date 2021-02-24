import { Constant } from './../infrastructure/constant';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStoreService {

  constructor() { }

  public get zoneId(): string {
    return localStorage.getItem(Constant.TIME_ZONE_KEY);
  }

  public set zoneId(z: string) {
    localStorage.setItem(Constant.TIME_ZONE_KEY, z);
  }



}
