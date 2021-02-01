import { TestBed } from '@angular/core/testing';

import { SavedFlowClientService } from './saved-flow-client.service';

describe('SavedFlowClientService', () => {
  let service: SavedFlowClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SavedFlowClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
