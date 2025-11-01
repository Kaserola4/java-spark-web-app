package com.pikolinc.dto.response;

import com.pikolinc.domain.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferResponseDto {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long itemId;
    private String itemName;
    private String itemDescription;
    private Double itemPrice;
    private Double amount;
    private Timestamp createdAt;
    private OfferStatus status;
}
