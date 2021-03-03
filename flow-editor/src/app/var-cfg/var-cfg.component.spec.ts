import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VarCfgComponent } from './var-cfg.component';

describe('VarCfgComponent', () => {
  let component: VarCfgComponent;
  let fixture: ComponentFixture<VarCfgComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VarCfgComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VarCfgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
