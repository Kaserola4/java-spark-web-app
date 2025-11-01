package com.pikolinc.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OfferCreateDto(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Item ID is required")
        Long itemId,
        @NotNull(message = "Final price is required")
        @Positive(message = "Final price must be greater than zero")
        Double finalPrice
) {
}
