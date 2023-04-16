import { TestBed } from '@angular/core/testing';

import { PricingFeedService } from './pricing-feed.service';

describe('PricingFeedService', () => {
  let service: PricingFeedService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PricingFeedService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
