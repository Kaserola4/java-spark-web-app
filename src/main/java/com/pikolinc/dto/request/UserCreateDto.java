package com.pikolinc.dto.request;

public record UserCreateDto(
        String name,
        Integer age,
        String email
) {
}
