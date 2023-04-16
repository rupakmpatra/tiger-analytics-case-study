package org.subrat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.subrat.model.PricingFeed;
import org.subrat.model.PricingFeedId;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class CustomPricingFeedFieldSetMapper extends BeanWrapperFieldSetMapper<PricingFeed> {
    Logger logger = LoggerFactory.getLogger(CustomPricingFeedFieldSetMapper.class);
    @Override
    public PricingFeed mapFieldSet(FieldSet fieldSet) throws BindException {
        //PricingFeed pricingFeed = super.mapFieldSet(fieldSet);

        if(!StringUtils.hasLength(fieldSet.readString("storeId")) ||
                !StringUtils.hasLength(fieldSet.readString("sku"))) {
            return null;
        }
        // Custom mappings
        PricingFeed pricingFeed = new PricingFeed();
        PricingFeedId pricingFeedId = new PricingFeedId();
        pricingFeedId.setStoreId(fieldSet.readString("storeId"));
        pricingFeedId.setSku(fieldSet.readString("sku"));
        pricingFeed.setId(pricingFeedId);
        pricingFeed.setProductName(fieldSet.readString("productName"));
        pricingFeed.setPrice(fieldSet.readBigDecimal("price"));
        LocalDateTime date;
        try {
            date = LocalDateTime.parse(fieldSet.readString("date"), DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException ex) {
            logger.error("Error parsing date: " + fieldSet.readString("date"), ex);
            date = LocalDateTime.now();
        }
        pricingFeed.setDate(date);
        return pricingFeed;
    }
}
