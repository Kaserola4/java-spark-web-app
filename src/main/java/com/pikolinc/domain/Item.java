package com.pikolinc.domain;

import lombok.Data;

@Data
public class Item {
    private Long id;
    private String name;
    private String description;
    private Double price;
}
