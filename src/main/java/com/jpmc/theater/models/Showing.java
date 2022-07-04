package com.jpmc.theater.models;

import com.google.common.annotations.VisibleForTesting;
import com.jpmc.theater.providers.LocalDateProvider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@EqualsAndHashCode
@ToString
public class Showing {
    private static int INITIAL_SEQUENCE = 0;

    @NonNull
    private final Movie movie;
    private final int sequenceOfTheDay;
    @NonNull
    private final LocalDateTime showStartTime;

    /**
     * Custom contructor that builds a Showing object based on a given Movie and LocalTime.
     * The constructor will automatically set the sequence of the day depending on previous
     * sequences of the day and will format the LocalTime into a LocalDateTime with the
     * current date and given LocalTime.
     *
     * @param movie The movie the showing is about.
     * @param showHourMinuteStartTime The LocalTime object that defines the hour and minute start date of the show.
     */
    public Showing(final @NonNull Movie movie, final LocalTime showHourMinuteStartTime) {
        this.movie = movie;
        this.sequenceOfTheDay = setSequenceOfTheDay();
        this.showStartTime = setShowStartTime(showHourMinuteStartTime);
    }

    @VisibleForTesting
    public Showing(final @NonNull Movie movie, final int sequenceOfTheDay, final @NonNull LocalDateTime localDateTime) {
        this.movie = movie;
        this.sequenceOfTheDay = sequenceOfTheDay;
        this.showStartTime = localDateTime;
    }

    private int setSequenceOfTheDay() {
        //Note: Every time we add a new showing, we increase the sequence automatically.
        //This way, we won't encounter issues where two showings will have the same
        //sequence.
        return INITIAL_SEQUENCE++;
    }

    private LocalDateTime setShowStartTime(final LocalTime showHourMinuteStartTime) {
        //Note: By retrieving the current date, we append the show's hour and minute start time
        //to create a LocalDateTime in the format "YYYY-MM-DD HH:MM:SS"
        return LocalDateTime.of(LocalDateProvider.currentDate(), showHourMinuteStartTime);
    }

    public BigDecimal getMovieTicketPrice() {
        return this.movie.getTicketPrice();
    }

    public Duration getMovieRunningTime() {
        return this.movie.getRunningTime();
    }

    public String getMovieTitle() {
        return this.movie.getTitle();
    }
}
