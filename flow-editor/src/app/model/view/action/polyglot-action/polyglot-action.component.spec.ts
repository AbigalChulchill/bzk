import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PolyglotActionComponent } from './polyglot-action.component';

describe('PolyglotActionComponent', () => {
  let component: PolyglotActionComponent;
  let fixture: ComponentFixture<PolyglotActionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PolyglotActionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PolyglotActionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
