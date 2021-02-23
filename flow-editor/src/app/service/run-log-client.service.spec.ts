import { TestBed } from '@angular/core/testing';

import { RunLogClientService } from './run-log-client.service';

describe('RunLogClientService', () => {
  let service: RunLogClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RunLogClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
