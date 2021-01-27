import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HttpActionComponent } from './http-action.component';

describe('HttpActionComponent', () => {
  let component: HttpActionComponent;
  let fixture: ComponentFixture<HttpActionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HttpActionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HttpActionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
