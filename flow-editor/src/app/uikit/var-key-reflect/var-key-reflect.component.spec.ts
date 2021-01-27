import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VarKeyReflectComponent } from './var-key-reflect.component';

describe('VarKeyReflectComponent', () => {
  let component: VarKeyReflectComponent;
  let fixture: ComponentFixture<VarKeyReflectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VarKeyReflectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VarKeyReflectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
