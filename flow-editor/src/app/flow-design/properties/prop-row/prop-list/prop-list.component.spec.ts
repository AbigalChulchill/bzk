import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PropListComponent } from './prop-list.component';

describe('PropListComponent', () => {
  let component: PropListComponent;
  let fixture: ComponentFixture<PropListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PropListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PropListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
