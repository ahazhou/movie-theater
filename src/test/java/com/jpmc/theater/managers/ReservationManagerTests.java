package com.jpmc.theater.managers;

import com.jpmc.theater.models.Customer;
import com.jpmc.theater.models.Movie;
import com.jpmc.theater.models.Reservation;
import com.jpmc.theater.models.Showing;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ReservationManagerTests {
    final static int TICKET_COUNT = 3;
    final static BigDecimal MOVIE_FEE = new BigDecimal("12.5");
    final static String CUSTOMER_NAME = "John Doe";
    final static String CUSTOMER_ID = "unused-id";
    final static String MOVIE_NAME = "Spider-Man: No Way Home";
    final static Duration MOVIE_DURATION = Duration.ofMinutes(90L);
    final static int MOVIE_SPECIAL_CODE = 1;
    final static int SHOW_START_HOUR = 1;
    final static int SHOW_START_MINUTE = 3;

    @Mock
    private MovieManager movieManager;
    private ReservationManager reservationManager;

    private AutoCloseable autoCloseable;

    private Reservation reservation;
    private Movie movie;
    @BeforeEach
    public void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        reservationManager = new ReservationManager(movieManager);

        final Customer customer = new Customer(CUSTOMER_NAME, CUSTOMER_ID);
        movie = new Movie(MOVIE_NAME, MOVIE_DURATION, MOVIE_FEE, MOVIE_SPECIAL_CODE);
        final Showing showing = new Showing(
                movie,
                LocalTime.of(SHOW_START_HOUR, SHOW_START_MINUTE)
        );
        reservation = new Reservation(customer, showing, TICKET_COUNT);

        when(movieManager.getMovieFee(movie)).thenReturn(MOVIE_FEE);
    }

    @AfterEach
    public void cleanUp() throws Exception {
        autoCloseable.close();
        verify(movieManager).getMovieFee(movie);

        verifyNoMoreInteractions(movieManager);
    }

    @Test
    void testGetTotalFee() {
        final BigDecimal totalFeeResult = reservationManager.getTotalFee(reservation);

        final BigDecimal expectedFeeResult = MOVIE_FEE.multiply(BigDecimal.valueOf(TICKET_COUNT));
        assertEquals(expectedFeeResult, totalFeeResult);
    }
}
