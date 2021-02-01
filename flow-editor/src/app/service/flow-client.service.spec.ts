import { TestBed } from '@angular/core/testing';

import { FlowClientService } from './flow-client.service';

describe('FlowClientService', () => {
  let service: FlowClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FlowClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
