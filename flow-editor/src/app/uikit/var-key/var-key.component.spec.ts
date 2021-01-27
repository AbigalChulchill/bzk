import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VarKeyComponent } from './var-key.component';

describe('VarKeyComponent', () => {
  let component: VarKeyComponent;
  let fixture: ComponentFixture<VarKeyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VarKeyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VarKeyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
