import { TestBed } from '@angular/core/testing';

import { VarCfgService } from './var-cfg.service';

describe('VarCfgService', () => {
  let service: VarCfgService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VarCfgService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
