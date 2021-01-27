import { ConditionInclude } from './../../../condition';
import { Component, OnInit } from '@angular/core';
import { Condition } from 'src/app/model/condition';
import { ClazzExComponent } from 'src/app/utils/prop-utils';

@Component({
  selector: 'app-condition-include',
  templateUrl: './condition-include.component.html',
  styleUrls: ['./condition-include.component.css']
})
export class ConditionIncludeComponent implements OnInit, ClazzExComponent {

  private data: any;
  constructor() { }

  public get condition(): ConditionInclude {
    return this.data;
  }


  init(d: any, mi: any): void {
    this.data = d;
  }


  ngOnInit(): void {
  }

}
