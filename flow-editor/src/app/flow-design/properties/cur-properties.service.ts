import { Prop, PropType, PropUtils } from './../../utils/prop-utils';
import { LanguageService } from './../../service/language.service';
import { Injectable } from '@angular/core';
import 'reflect-metadata';
import { Flow } from 'src/app/model/flow';



@Injectable({
  providedIn: 'root'
})
export class CurPropertiesService {


  private target: object;
  public props = new Array<Prop>();

  constructor(
    private language: LanguageService
  ) {

  }

  public setTarget(t: object): void {
    this.target = t;
    this.props = new Array<Prop>();
    PropUtils.getInstance().list(this.target).then(p => this.props = p);
  }


  public hasTarget(): boolean {
    return this.target != null;
  }

}

// TODO move to prop utils

