package com.pikolinc.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferRebidDto {
    @NotNull(message = "amount is required")
    @Positive(message = "amount has to be positive")
    private Double amount;
}
