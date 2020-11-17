import { ModelValidAtion } from './../../service/model-valid-ation';
import { Component, Input, OnInit } from '@angular/core';
import { ToastService } from 'src/app/service/toast.service';
import { BzkObj } from '../../bzk-obj';
import { ModelUpdateAdapter } from '../../service/model-update-adapter';

@Component({
  selector: 'app-task-func-view',
  templateUrl: './task-func-view.component.html',
  styleUrls: ['./task-func-view.component.css']
})
export class TaskFuncViewComponent implements OnInit {

  @Input() task: BzkObj;

  constructor(
    private toast: ToastService
  ) { }

  ngOnInit(): void {
  }

  public delete(): void {
    const f = ModelUpdateAdapter.getInstance().getFlow();

    try {
      ModelValidAtion.removeTask(f, this.task.uid);
    } catch (ex) {
      this.toast.twinkle({
        msg: ex,
        title: ''
      });
    }

    ModelUpdateAdapter.getInstance().onRefleshCart();
  }

}
