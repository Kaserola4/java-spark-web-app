package com.pikolinc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model representing an application user.
 *<br>
 * Fields:
 * <ul>
 *     <li>id,</li>
 *     <li>name</li>
 *     <li>email</li>
 *     <li>age</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private Integer age;
}
