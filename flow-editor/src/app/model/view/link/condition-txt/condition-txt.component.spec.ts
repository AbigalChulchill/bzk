import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConditionTxtComponent } from './condition-txt.component';

describe('ConditionTxtComponent', () => {
  let component: ConditionTxtComponent;
  let fixture: ComponentFixture<ConditionTxtComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConditionTxtComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConditionTxtComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
