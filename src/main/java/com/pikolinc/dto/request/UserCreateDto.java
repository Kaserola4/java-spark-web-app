package com.pikolinc.dto.request;

import jakarta.validation.constraints.*;

public record UserCreateDto(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s'-]+$", message = "Name must contain only letters, spaces, hyphens and apostrophes")
        String name,

        @NotNull(message = "Age is required")
        @Min(value = 18, message = "Age must be at least 18")
        Integer age,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must not exceed 255 characters")
        String email
) {
}