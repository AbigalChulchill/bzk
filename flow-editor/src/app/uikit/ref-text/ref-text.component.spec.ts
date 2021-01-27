import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RefTextComponent } from './ref-text.component';

describe('RefTextComponent', () => {
  let component: RefTextComponent;
  let fixture: ComponentFixture<RefTextComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RefTextComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RefTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
