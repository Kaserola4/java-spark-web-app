package com.pikolinc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model representing a collectible Item.
 * <br>
 * Fields:
 * <ul>
 *     <li>id: unique identifier</li>
 *     <li>name: item name</li>
 *     <li>description: textual description</li>
 *     <li>price: base price</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private Double price;
}
