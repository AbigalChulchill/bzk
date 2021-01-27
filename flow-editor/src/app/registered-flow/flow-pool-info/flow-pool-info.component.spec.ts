import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FlowPoolInfoComponent } from './flow-pool-info.component';

describe('FlowPoolInfoComponent', () => {
  let component: FlowPoolInfoComponent;
  let fixture: ComponentFixture<FlowPoolInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FlowPoolInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FlowPoolInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
