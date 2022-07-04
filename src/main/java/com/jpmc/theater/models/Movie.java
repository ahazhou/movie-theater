package com.jpmc.theater.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Duration;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Getter
public class Movie {
    public static final int MOVIE_CODE_SPECIAL = 1;

    @NonNull
    private final String title;
    //Note: By not marking description as final, we exclude it from the constructor.
    private String description;
    @NonNull
    private final Duration runningTime;
    @NonNull
    private final BigDecimal ticketPrice;
    private final int specialCode;

    public Movie(final Movie movie) {
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.runningTime = movie.getRunningTime();
        this.ticketPrice = movie.getTicketPrice();
        this.specialCode = movie.getSpecialCode();
    }
}