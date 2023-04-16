package org.subrat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.subrat.model.PricingFeedId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PricingFeedDto {
    private PricingFeedIdDto id;
    private String productName;
    private BigDecimal price;
    private LocalDateTime date;
}
