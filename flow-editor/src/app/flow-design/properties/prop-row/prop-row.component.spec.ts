import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PropRowComponent } from './prop-row.component';

describe('PropRowComponent', () => {
  let component: PropRowComponent;
  let fixture: ComponentFixture<PropRowComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PropRowComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PropRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
