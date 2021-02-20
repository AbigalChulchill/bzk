import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobSidebarComponent } from './job-sidebar.component';

describe('JobSidebarComponent', () => {
  let component: JobSidebarComponent;
  let fixture: ComponentFixture<JobSidebarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JobSidebarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
