import { TestBed } from '@angular/core/testing';

import { VarCfgClientService } from './var-cfg-client.service';

describe('VarCfgClientService', () => {
  let service: VarCfgClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VarCfgClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
