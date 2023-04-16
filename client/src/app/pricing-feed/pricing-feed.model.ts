import { PricingFeedId } from "./pricing-feed-id.model";

export class PricingFeed {
    constructor(
        public id?: PricingFeedId,
        public productName?: string,
        public price?: number,
        public date?: Date
      ) {}
}