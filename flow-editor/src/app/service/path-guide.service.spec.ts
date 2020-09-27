import { TestBed } from '@angular/core/testing';

import { PathGuideService } from './path-guide.service';

describe('PathGuideService', () => {
  let service: PathGuideService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PathGuideService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
