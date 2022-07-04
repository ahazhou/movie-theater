package com.jpmc.theater.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Reservation {
    @NonNull
    private final Customer customer;
    @NonNull
    private final Showing showing;
    private final int ticketCount;
}