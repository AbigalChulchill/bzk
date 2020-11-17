import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisteredFlowComponent } from './registered-flow.component';

describe('RegisteredFlowComponent', () => {
  let component: RegisteredFlowComponent;
  let fixture: ComponentFixture<RegisteredFlowComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegisteredFlowComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisteredFlowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
