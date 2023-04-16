import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PricingFeedComponent } from './pricing-feed.component';

describe('PricingFeedComponent', () => {
  let component: PricingFeedComponent;
  let fixture: ComponentFixture<PricingFeedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PricingFeedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PricingFeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
