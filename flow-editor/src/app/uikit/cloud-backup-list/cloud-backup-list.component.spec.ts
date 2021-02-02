import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CloudBackupListComponent } from './cloud-backup-list.component';

describe('CloudBackupListComponent', () => {
  let component: CloudBackupListComponent;
  let fixture: ComponentFixture<CloudBackupListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CloudBackupListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CloudBackupListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
