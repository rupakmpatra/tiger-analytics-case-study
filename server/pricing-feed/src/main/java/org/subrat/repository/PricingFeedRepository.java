package org.subrat.repository;

import io.micrometer.core.instrument.binder.db.MetricsDSLContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.subrat.model.PricingFeed;
import org.subrat.model.PricingFeedId;

import java.util.List;

@Repository
public interface PricingFeedRepository extends JpaRepository<PricingFeed, PricingFeedId> {
    Page<PricingFeed> findAll(Specification<PricingFeed> spec, Pageable pageable);
}
