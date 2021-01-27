import { ClazzExComponent } from './../../utils/prop-utils';
import { Component, OnInit } from '@angular/core';
import { VarKeyReflect } from 'src/app/infrastructure/meta';

@Component({
  selector: 'app-var-key-reflect',
  templateUrl: './var-key-reflect.component.html',
  styleUrls: ['./var-key-reflect.component.css']
})
export class VarKeyReflectComponent implements OnInit,ClazzExComponent {

  public data:VarKeyReflect;

  init(d: any, mataInfo: any): void {
    this.data = d;
  }

  constructor() { }


  ngOnInit(): void {
  }

}
