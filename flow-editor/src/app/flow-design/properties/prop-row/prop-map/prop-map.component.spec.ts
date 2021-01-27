import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PropMapComponent } from './prop-map.component';

describe('PropMapComponent', () => {
  let component: PropMapComponent;
  let fixture: ComponentFixture<PropMapComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PropMapComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PropMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
