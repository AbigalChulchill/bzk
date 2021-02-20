import { TestBed } from '@angular/core/testing';

import { JobClientService } from './job-client.service';

describe('JobClientService', () => {
  let service: JobClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JobClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
