import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PropRefComponent } from './prop-ref.component';

describe('PropRefComponent', () => {
  let component: PropRefComponent;
  let fixture: ComponentFixture<PropRefComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PropRefComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PropRefComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
