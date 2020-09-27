import { TestBed } from '@angular/core/testing';

import { ModifyingFlowService } from './modifying-flow.service';

describe('ModifyingFlowService', () => {
  let service: ModifyingFlowService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ModifyingFlowService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
