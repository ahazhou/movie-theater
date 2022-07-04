package com.jpmc.theater.managers;

import com.google.inject.Inject;
import com.jpmc.theater.models.Movie;
import com.jpmc.theater.models.Reservation;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.log4j.Logger;

import javax.inject.Singleton;
import java.math.BigDecimal;

@AllArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class ReservationManager {
    final Logger logger = Logger.getLogger(ReservationManager.class);
    final MovieManager movieManager;
    public BigDecimal getTotalFee(@NonNull final Reservation reservation) {
        final Movie movie = reservation.getShowing().getMovie();
        final BigDecimal movieFee = movieManager.getMovieFee(movie);
        final int ticketCount = reservation.getTicketCount();
        logger.debug(String.format("Calculating reservation's total fee for movie %s with %s ticket(s)", movie.getRunningTime(), ticketCount));
        return movieFee.multiply(BigDecimal.valueOf(ticketCount));
    }
}
