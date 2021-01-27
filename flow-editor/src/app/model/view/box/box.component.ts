import { ToastService } from 'src/app/service/toast.service';
import { ModelValidAtion } from './../../service/model-valid-ation';
import { ModelUpdateAdapter } from './../../service/model-update-adapter';
import { CommUtils } from 'src/app/utils/comm-utils';
import { BzkUtils } from './../../../utils/bzk-utils';
import { Action, NodejsAction } from './../../action';
import { Component, Host, Inject, OnInit } from '@angular/core';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { Box, Link } from '../../box';
import { ModelUtils } from 'src/app/utils/model-utils';
import { Constant } from 'src/app/infrastructure/constant';
import { ConditionNum } from '../../condition';
import { PropertiesComponent } from 'src/app/flow-design/properties/properties.component';

@Component({
  selector: 'app-box',
  templateUrl: './box.component.html',
  styleUrls: ['./box.component.css']
})
export class BoxComponent implements OnInit, ClazzExComponent {
  data: any;
  actionClzChoosing = false;
  actionClazz = '';

  constructor(
    private toast: ToastService,
    @Inject(PropertiesComponent) private parentView: PropertiesComponent
  ) { }

  public get box(): Box {
    return this.data;
  }

  init(d: any, mi: any): void {
    this.data = d;
  }


  ngOnInit(): void {
  }

  public toCreateAction(): void {
    this.actionClzChoosing = true;
  }

  public createAction(): void {
    ModelUtils.createAction(this.actionClazz, this.box);
    ModelUpdateAdapter.getInstance().onRefleshCart();
  }

  public listActionClazz(): Array<string> {
    return BzkUtils.listOtypeKeys(Action);
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


  public get whileEnabled(): boolean { return this.box.whileJudgment != null; }

  public set whileEnabled(b: boolean) {
    if (b) {
      if (!this.box.whileJudgment){
        this.box.whileJudgment = ConditionNum.gen();
      }
    } else {
      this.box.whileJudgment = null;
    }
    this.parentView.curProperties.reflesh();
  }


}
