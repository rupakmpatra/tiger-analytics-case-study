package org.subrat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.subrat.dto.PricingFeedDto;
import org.subrat.model.PricingFeed;
import org.subrat.model.PricingFeedId;
import org.subrat.repository.PricingFeedRepository;
import org.subrat.util.PricingFeedFilter;
import org.subrat.util.PricingFeedSpecifications;

@Service
@Transactional
@RequiredArgsConstructor
public class PricingFeedService {
    private final ObjectMapper objectMapper;
    private final PricingFeedRepository pricingFeedRepository;

    public Page<PricingFeedDto> findAll(PricingFeedFilter filter, Pageable pageable) {
        // Use the filter fields in query
        Specification<PricingFeed> spec = Specification.where(null);
        if (filter.getStoreId() != null) {
            spec = spec.and(PricingFeedSpecifications.containStoreId(filter.getStoreId()));
        }
        if (filter.getSku() != null) {
            spec = spec.and(PricingFeedSpecifications.containSku(filter.getSku()));
        }
        if (filter.getProductName() != null) {
            spec = spec.and(PricingFeedSpecifications.containProductName(filter.getProductName()));
        }
        if (filter.getPrice() != null) {
            spec = spec.and(PricingFeedSpecifications.hasPrice(filter.getPrice()));
        }
        if (filter.getDate() != null) {
            spec = spec.and(PricingFeedSpecifications.hasDate(filter.getDate()));
        }
        return pricingFeedRepository.findAll(spec, pageable)
                .map(pricingFeed -> objectMapper.convertValue(pricingFeed, PricingFeedDto.class));
    }

    public PricingFeedDto pricingFeedById(String storeId, String sku) throws Exception {
        PricingFeed pricingFeed = pricingFeedRepository.findById(new PricingFeedId(storeId, sku))
                .orElseThrow(() -> new Exception("Pricing feed details not available against "+storeId+" and "+sku));
        return objectMapper.convertValue(pricingFeed, PricingFeedDto.class);
    }

    public PricingFeedDto save(PricingFeedDto pricingFeedDto) {
        PricingFeed pricingFeed = objectMapper.convertValue(pricingFeedDto, PricingFeed.class);
        pricingFeedRepository.save(pricingFeed);
        return objectMapper.convertValue(pricingFeed, PricingFeedDto.class);
    }

    public PricingFeedDto update(String storeId, String sku, PricingFeedDto pricingFeedDto) throws Exception {
        PricingFeed pricingFeed = pricingFeedRepository.findById(new PricingFeedId(storeId, sku))
                .orElseThrow(() -> new Exception("Pricing details not updated, because data not available against "+storeId+" and "+sku));
        pricingFeed.setPrice(pricingFeed.getPrice());
        pricingFeed.setProductName(pricingFeedDto.getProductName());
        pricingFeed.setDate(pricingFeedDto.getDate());
        pricingFeedRepository.save(pricingFeed);
        return objectMapper.convertValue(pricingFeed, PricingFeedDto.class);
    }

    public String deletePricingFeed(String storeId, String sku) throws Exception {
        PricingFeed pricingFeed = pricingFeedRepository.findById(new PricingFeedId(storeId, sku))
                .orElseThrow(() -> new Exception("Not able to delete because data not available against "+storeId+" and "+sku));
        pricingFeedRepository.delete(pricingFeed);
        return "Pricing feed deleted successfully !";
    }

    public Long totalCount() {
        return pricingFeedRepository.count();
    }
}
