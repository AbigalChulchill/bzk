import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConditionNumComponent } from './condition-num.component';

describe('ConditionNumComponent', () => {
  let component: ConditionNumComponent;
  let fixture: ComponentFixture<ConditionNumComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConditionNumComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConditionNumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
