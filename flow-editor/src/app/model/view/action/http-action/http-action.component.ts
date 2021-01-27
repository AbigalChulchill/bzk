import { PropUtils } from './../../../../utils/prop-utils';
import { HttpAction } from './../../../action';
import { Component, OnInit } from '@angular/core';
import { ClazzExComponent, Prop } from 'src/app/utils/prop-utils';
import { ActionComponent } from '../action.component';

@Component({
  selector: 'app-http-action',
  templateUrl: './http-action.component.html',
  styleUrls: ['./http-action.component.css']
})
export class HttpActionComponent extends ActionComponent implements OnInit, ClazzExComponent {

  public HeaderSetType = HeaderSetType;
  public curHeaderType = HeaderSetType.headRef;
  public headerProp: Prop;
  ngOnInit(): void {
    this.changeHeaderType(HeaderSetType.headRef);
  }

  public get httpAction(): HttpAction { return this.data; }

  init(d: any, mataInfo: any): void {
    this.data = d;
  }
  getData(): any {
    return this.data;
  }

  public changeHeaderType(t: HeaderSetType): void {
    this.curHeaderType = t;
    this.headerProp = this.genHeaderProp();
  }

  private genHeaderProp(): Prop {
    return PropUtils.getInstance().gen(this.curHeaderType, this.httpAction);
  }
}




export enum HeaderSetType {
  headersFlat = 'headersFlat', headRef = 'headRef'
}
