import { Component, OnInit } from '@angular/core';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { VarsStore } from '../../vars-store';
import { plainToClass } from 'class-transformer';
import { BaseVar, TextProvide } from 'src/app/infrastructure/meta';

@Component({
  selector: 'app-var-store',
  templateUrl: './var-store.component.html',
  styleUrls: ['./var-store.component.css']
})
export class VarStoreComponent implements OnInit, ClazzExComponent {

  data: any;
  list = new Array<VProvide>();
  constructor() { }

  init(d: any): void {
    this.data = d;
    this.list = this.listVProvide();
  }


  public get varStore(): VarsStore {
    return this.data;
  }

  ngOnInit(): void {
  }

  private listVProvide(): Array<VProvide> {
    const ans = new Array<VProvide>();
    const ks = Object.keys(this.varStore);
    for (const k of ks) {
      ans.push(new VProvide(this.varStore, k));
    }

    return ans;
  }

  public create(key: string): void {
    this.varStore.setVars(key, new BaseVar());
    this.list = this.listVProvide();
  }

}

class VProvide implements TextProvide {

  public varStore: VarsStore;
  public key: string;

  constructor(v: VarsStore, k: string) {
    this.varStore = v;
    this.key = k;
  }

  getStr(): string {
    return JSON.stringify(this.varStore.getVars(this.key));

  }

  setStr(d: string): void {
    const o = JSON.parse(d);
    this.varStore.setVars(this.key, plainToClass(BaseVar, o));

  }


}
