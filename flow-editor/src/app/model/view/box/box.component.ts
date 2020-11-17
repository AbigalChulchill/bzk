import { ToastService } from 'src/app/service/toast.service';
import { ModelValidAtion } from './../../service/model-valid-ation';
import { ModelUpdateAdapter } from './../../service/model-update-adapter';
import { CommUtils } from 'src/app/utils/comm-utils';
import { BzkUtils } from './../../../utils/bzk-utils';
import { NodejsAction } from './../../action';
import { Component, OnInit } from '@angular/core';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { Box, Link } from '../../box';
import { ModelUtils } from 'src/app/utils/model-utils';

@Component({
  selector: 'app-box',
  templateUrl: './box.component.html',
  styleUrls: ['./box.component.css']
})
export class BoxComponent implements OnInit, ClazzExComponent {
  data: any;

  constructor(
    private toast: ToastService
  ) { }

  public get box(): Box {
    return this.data;
  }

  init(d: any): void {
    this.data = d;
  }
  getData(): any {
    return this.box;
  }

  ngOnInit(): void {
  }

  public createAction(): void {
    ModelUtils.createAction(this.box);
    ModelUpdateAdapter.getInstance().onRefleshCart();
  }

  public createLink(): void {
    ModelUtils.createLink(this.box);
    ModelUpdateAdapter.getInstance().onRefleshCart();
  }

  public deleteMe(): void {
    try {
      ModelValidAtion.removeBox(ModelUpdateAdapter.getInstance().getFlow(), this.box.uid);
      ModelUpdateAdapter.getInstance().onRefleshCart();
    } catch (ex) {
      this.toast.twinkle({
        msg: ex,
        title: ''
      });
    }
  }


}
