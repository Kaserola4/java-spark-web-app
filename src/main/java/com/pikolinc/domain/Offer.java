package com.pikolinc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer {
    private Long id;
    private Long userId;
    private Long itemId;
    private Double amount;
    private LocalDateTime createdAt;
}
