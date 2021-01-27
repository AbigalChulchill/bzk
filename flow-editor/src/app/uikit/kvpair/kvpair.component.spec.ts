import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KVPairComponent } from './kvpair.component';

describe('KVPairComponent', () => {
  let component: KVPairComponent;
  let fixture: ComponentFixture<KVPairComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KVPairComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KVPairComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
