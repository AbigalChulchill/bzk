import { RefTextComponent } from './../ref-text/ref-text.component';
import { TextProvide } from './../../infrastructure/meta';
import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { VarKey } from 'src/app/model/var-key';
import { ClazzExComponent } from 'src/app/utils/prop-utils';

@Component({
  selector: 'app-var-key',
  templateUrl: './var-key.component.html',
  styleUrls: ['./var-key.component.css']
})
export class VarKeyComponent implements AfterViewInit, ClazzExComponent, TextProvide {


  @Input() public key: VarKey;

  constructor() { }


  init(d: any, mataInfo: any): void {
    this.key = d;
  }


  ngAfterViewInit(): void {

  }



  getStr(): string {
    if (!this.key) return null;
    return this.key.getExpress();
  }
  setStr(d: string): void {
    if (!this.key) return;
    this.key.setByExpress(d);
  }

}
