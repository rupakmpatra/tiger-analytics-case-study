package org.subrat.util;

import org.springframework.data.jpa.domain.Specification;
import org.subrat.model.PricingFeed;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PricingFeedSpecifications {
    public static Specification<PricingFeed> containStoreId(String storeId) {
        return (root, query, builder) -> builder.like(root.get("id").get("storeId"), storeId);
    }

    public static Specification<PricingFeed> containSku(String sku) {
        return (root, query, builder) -> builder.like(root.get("id").get("sku"), sku);
    }
    public static Specification<PricingFeed> containProductName(String productName) {
        return (root, query, builder) -> builder.like(root.get("productName"), productName);
    }

    public static Specification<PricingFeed> hasPrice(BigDecimal price) {
        return (root, query, builder) -> builder.equal(root.get("price"), price);
    }

    public static Specification<PricingFeed> hasDate(LocalDateTime date) {
        return (root, query, builder) -> builder.equal(root.get("date"), date);
    }

}
