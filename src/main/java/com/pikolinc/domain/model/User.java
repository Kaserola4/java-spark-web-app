package com.pikolinc.domain.model;

public record User(
        Long id,
        String name,
        String email,
        Integer age
){
}
