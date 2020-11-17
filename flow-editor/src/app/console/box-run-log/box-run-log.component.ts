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

  ReadJsonProvide = ReadJsonProvide;

  @Input() public data: BoxRunLog;

  constructor() { }

  ngOnInit(): void {
  }

  public getTest(): string {
    return JSON.stringify(this.data);
  }

  public getWidthClass(): string {
    return this.isAction() ? 'col-4' : 'col-5';
  }

  public isAction(): boolean {
    const b = this.data.constructor === ActionRunLog;
    return b;
  }

  public toAction(): ActionRunLog {
    return this.data as ActionRunLog;
  }

}
