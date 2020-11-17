import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VarStoreComponent } from './var-store.component';

describe('VarStoreComponent', () => {
  let component: VarStoreComponent;
  let fixture: ComponentFixture<VarStoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VarStoreComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VarStoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
