import { ModelUtils } from 'src/app/utils/model-utils';
import { ModelValidAtion } from './../../service/model-valid-ation';
import { Component, Input, OnInit } from '@angular/core';
import { ToastService } from 'src/app/service/toast.service';
import { BzkObj } from '../../bzk-obj';
import { ModelUpdateAdapter } from '../../service/model-update-adapter';
import { Box } from '../../box';

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

  public up(): void {
    this.shfit(b => {
      ModelUtils.shiftTaskToBefore(b, this.task.uid);
    });
  }

  public down(): void {
    this.shfit(b => {
      ModelUtils.shiftTaskToNext(b, this.task.uid);
    });
  }

  private shfit(a: (b: Box) => void): void {
    const box = this.getBox();
    a(box);
    ModelUpdateAdapter.getInstance().onRefleshCart();
  }

  private getBox(): Box {
    const f = ModelUpdateAdapter.getInstance().getFlow();
    return f.getBoxByChild(this.task.uid);
  }

  public isLast(): boolean {
    const box = this.getBox();
    if (!box) return false;
    return box.taskSort[box.taskSort.length - 1] === this.task.uid;
  }

  public isFirst(): boolean {
    const box = this.getBox();
    if (!box) return false;
    return box.taskSort[0] === this.task.uid;
  }

}
