import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BoxRunLogComponent } from './box-run-log.component';

describe('BoxRunLogComponent', () => {
  let component: BoxRunLogComponent;
  let fixture: ComponentFixture<BoxRunLogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BoxRunLogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BoxRunLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
