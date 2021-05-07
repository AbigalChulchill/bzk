import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConditionTimeComponent } from './condition-time.component';

describe('ConditionTimeComponent', () => {
  let component: ConditionTimeComponent;
  let fixture: ComponentFixture<ConditionTimeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConditionTimeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConditionTimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
