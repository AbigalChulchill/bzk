import { TestBed } from '@angular/core/testing';

import { CurPropertiesService } from './cur-properties.service';

describe('CurPropertiesService', () => {
  let service: CurPropertiesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CurPropertiesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
