import { TestBed } from '@angular/core/testing';

import { SVGZoomService } from './svgzoom.service';

describe('SVGZoomService', () => {
  let service: SVGZoomService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SVGZoomService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
