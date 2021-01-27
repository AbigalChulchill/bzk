import { ClazzExComponent } from './../../../utils/prop-utils';
import { Component, OnInit } from '@angular/core';
import { Transition } from '../../transition';
import { StringUtils } from 'src/app/utils/string-utils';
import { ModelUpdateAdapter } from '../../service/model-update-adapter';
import { ModelUtils } from 'src/app/utils/model-utils';

@Component({
  selector: 'app-transition',
  templateUrl: './transition.component.html',
  styleUrls: ['./transition.component.css']
})
export class TransitionComponent implements OnInit, ClazzExComponent {
  StringUtils = StringUtils;
  public data: Transition;

  init(d: any, mataInfo: any): void {
    this.data = d;
  }


  constructor() { }

  ngOnInit(): void {
  }

  public removeLink(): void {
    this.data.toBox = '';
    ModelUpdateAdapter.getInstance().updater.onLinkBox(null);
    ModelUpdateAdapter.getInstance().onRefleshCart();
  }

  public setEnd(): void {
    this.data.endTag = 'TODO Why End';
    this.data.toBox = '';
  }

  public linkBox(): void {
    ModelUpdateAdapter.getInstance().updater.onLinkBox(b => {
      this.data.endTag = null;
      this.data.toBox = b.uid;
      ModelUpdateAdapter.getInstance().updater.onLinkBox(null);
      ModelUpdateAdapter.getInstance().onRefleshCart();
    });

  }

  public createBox(): void {
    const b = ModelUtils.createSimpleBox(ModelUpdateAdapter.getInstance().getFlow());
    this.data.toBox = b.uid;
    this.data.endTag = null;
    ModelUpdateAdapter.getInstance().onRefleshCart();
  }

  public isEnd(): boolean {
    return this.data.endTag != null && StringUtils.isBlank(this.data.toBox);
  }

}
