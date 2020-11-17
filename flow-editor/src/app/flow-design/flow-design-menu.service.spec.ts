import { TestBed } from '@angular/core/testing';

import { FlowDesignMenuService } from './flow-design-menu.service';

describe('FlowDesignMenuService', () => {
  let service: FlowDesignMenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FlowDesignMenuService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
