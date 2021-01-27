import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConditionIncludeComponent } from './condition-include.component';

describe('ConditionIncludeComponent', () => {
  let component: ConditionIncludeComponent;
  let fixture: ComponentFixture<ConditionIncludeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConditionIncludeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConditionIncludeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
