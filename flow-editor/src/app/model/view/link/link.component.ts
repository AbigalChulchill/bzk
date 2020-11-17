import { ModelUtils } from './../../../utils/model-utils';
import { ModelUpdateAdapter } from './../../service/model-update-adapter';
import { StringUtils } from './../../../utils/string-utils';
import { Component, OnInit } from '@angular/core';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { Box, Link } from '../../box';

@Component({
  selector: 'app-link',
  templateUrl: './link.component.html',
  styleUrls: ['./link.component.css']
})
export class LinkComponent implements OnInit, ClazzExComponent {
  StringUtils = StringUtils;
  data: any;
  constructor() { }

  public get link(): Link { return this.data; }
  init(d: any): void {
    this.data = d;
  }


  getData(): any {
    return this.link;
  }


  ngOnInit(): void {
  }

  public setEnd(): void {
    this.link.endTag = this.link.uid + ' End';
  }

  public createBox(): void {
    const b = ModelUtils.createSimpleBox(ModelUpdateAdapter.getInstance().getFlow());
    this.link.toBox = b.uid;
    this.link.endTag = null;
    ModelUpdateAdapter.getInstance().onRefleshCart();
  }

  public linkBox(): void {
    ModelUpdateAdapter.getInstance().updater.onLinkBox(b => {
      this.link.endTag = null;
      this.link.toBox = b.uid;
      ModelUpdateAdapter.getInstance().updater.onLinkBox(null);
      ModelUpdateAdapter.getInstance().onRefleshCart();
    });

  }

  public removeLink(): void {
    this.link.toBox = '';
    ModelUpdateAdapter.getInstance().updater.onLinkBox(null);
    ModelUpdateAdapter.getInstance().onRefleshCart();
  }

  public isLasted(): boolean {
    return ModelUpdateAdapter.getInstance().getFlow().getBoxByChild(this.link.uid).getLastLink().uid === this.link.uid;
  }

}
