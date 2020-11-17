import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskFuncViewComponent } from './task-func-view.component';

describe('TaskFuncViewComponent', () => {
  let component: TaskFuncViewComponent;
  let fixture: ComponentFixture<TaskFuncViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskFuncViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskFuncViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
