package com.jpmc.theater.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Customer {
    @NonNull
    private final String name;
    private final String id;
}