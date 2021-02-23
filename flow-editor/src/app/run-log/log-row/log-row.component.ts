import { RunLogComponent, ShowType } from './../run-log.component';
import { Component, Input, OnInit } from '@angular/core';
import { StringUtils } from 'src/app/utils/string-utils';
import { RunLog } from 'src/app/model/run-log';
import { ReadJsonProvide } from 'src/app/uikit/json-editor/json-editor.component';

@Component({
  selector: 'app-log-row',
  templateUrl: './log-row.component.html',
  styleUrls: ['./log-row.component.css']
})
export class LogRowComponent implements OnInit {
  ShowType = ShowType;
  StringUtils = StringUtils;
  ReadJsonProvide=ReadJsonProvide;

  @Input() public log: RunLog;

  constructor(
    public parentView: RunLogComponent
  ) { }

  ngOnInit(): void {
  }

  public getWidthClass(): string {
    return this.hasReturn() ? 'col-4' : 'col-5';
  }

  public hasReturn(): boolean {
    const vvs = this.log.varVals;
    if (!vvs) { return false; }
    if (vvs.length <= 0) { return false; }
    return true;
  }

  public isFullView(): boolean {
    if (this.parentView.showType === ShowType.Message) { return true; }
    if (this.parentView.showType === ShowType.OrgJson) { return true; }
    if (this.parentView.showType === ShowType.Vars) { return false; }
    throw new Error('not support :' + this.parentView.showType);
  }


}
