package com.pikolinc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Domain model representing an Offer (bid).
 *<br>
 * Fields:
 * <ul>
 *     <li>id</li>
 *     <li>userId</li>
 *     <li>itemId</li>
 *     <li>amount</li>
 *     <li>status</li>
 *     <li>createdAt</li>
 * </ul>
 * The default status is {@link com.pikolinc.domain.OfferStatus#OPEN}.
 */
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
