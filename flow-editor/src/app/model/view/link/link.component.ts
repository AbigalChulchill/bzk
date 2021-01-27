import { ModelUtils } from './../../../utils/model-utils';
import { ModelUpdateAdapter } from './../../service/model-update-adapter';
import { StringUtils } from './../../../utils/string-utils';
import { Component, OnInit } from '@angular/core';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { Box, Link } from '../../box';
import { Condition } from '../../condition';

@Component({
  selector: 'app-link',
  templateUrl: './link.component.html',
  styleUrls: ['./link.component.css']
})
export class LinkComponent implements OnInit, ClazzExComponent {
  StringUtils = StringUtils;
  data: any;
  constructor() { }


  getCondition(): Condition {
    return this.link.condition;
  }
  setCondition(c: Condition): void {
    this.link.condition = c;
  }


  public get link(): Link { return this.data; }
  init(d: any, mi: any): void {
    this.data = d;
  }




  ngOnInit(): void {
  }


}
