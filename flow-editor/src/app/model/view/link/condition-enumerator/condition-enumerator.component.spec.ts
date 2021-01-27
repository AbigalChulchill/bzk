import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConditionEnumeratorComponent } from './condition-enumerator.component';

describe('ConditionEnumeratorComponent', () => {
  let component: ConditionEnumeratorComponent;
  let fixture: ComponentFixture<ConditionEnumeratorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConditionEnumeratorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConditionEnumeratorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
