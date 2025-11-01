package com.pikolinc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
public class Offer {
    private Long id;
    private Long userId;
    private Long itemId;
    private Double amount;
    private OfferStatus status;
    private Timestamp createdAt;

    public Offer() {
        this.status = OfferStatus.OPEN;
    }
}
