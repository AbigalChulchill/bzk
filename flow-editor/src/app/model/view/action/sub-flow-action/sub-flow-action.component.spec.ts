import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubFlowActionComponent } from './sub-flow-action.component';

describe('SubFlowActionComponent', () => {
  let component: SubFlowActionComponent;
  let fixture: ComponentFixture<SubFlowActionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubFlowActionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubFlowActionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
