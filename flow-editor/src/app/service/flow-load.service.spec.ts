import { TestBed } from '@angular/core/testing';

import { FlowLoadService } from './flow-load.service';

describe('FlowLoadService', () => {
  let service: FlowLoadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FlowLoadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
