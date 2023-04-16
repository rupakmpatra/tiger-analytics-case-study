package org.subrat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PricingFeedId implements Serializable {
    @Column(name = "store_id", nullable = false)
    private String storeId;

    @Column(name = "sku", nullable = false)
    private String sku;
}
