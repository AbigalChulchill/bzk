import { StringUtils } from './../../utils/string-utils';
import { BoxRunLog, ActionRunLog } from './../../dto/console-dtos';
import { Component, Input, OnInit } from '@angular/core';
import { ReadJsonProvide } from 'src/app/uikit/json-editor/json-editor.component';
import { ActionRunLogRow } from '../console.component';

@Component({
  selector: 'app-box-run-log',
  templateUrl: './box-run-log.component.html',
  styleUrls: ['./box-run-log.component.css']
})
export class BoxRunLogComponent implements OnInit {
  ShowType = ShowType;
  StringUtils = StringUtils;
  ReadJsonProvide = ReadJsonProvide;
  @Input() public data: BoxRunLog;
  public showType = ShowType.Vars;

  constructor() { }

  ngOnInit(): void {
    if (this.data.failed) { this.showType = ShowType.Message; }
  }

  public getTest(): string {
    return JSON.stringify(this.data);
  }

  public getWidthClass(): string {
    return this.hasReturn() ? 'col-4' : 'col-5';
  }

  public hasReturn(): boolean {
    if (this.data.constructor === ActionRunLog) { return false; }
    const vvs = this.toAction().varVals;
    if (!vvs) { return false; }
    if (vvs.length <= 0) { return false; }
    return true;
  }

  public toAction(): ActionRunLog {
    return this.data as ActionRunLog;
  }

  public getPlainText(): string {
    if (this.showType === ShowType.Message) { return this.data.msg; }
    if (this.showType === ShowType.OrgPlain) { return this.data.orgText; }
    throw new Error('not support :' + this.showType);
  }

  public isFullView(): boolean {
    if (this.showType === ShowType.Message) { return true; }
    if (this.showType === ShowType.OrgPlain) { return true; }
    if (this.showType === ShowType.OrgJson) { return true; }
    if (this.showType === ShowType.Vars) { return false; }
    throw new Error('not support :' + this.showType);
  }

}

export enum ShowType {
  Vars, OrgPlain, OrgJson, Message
}
