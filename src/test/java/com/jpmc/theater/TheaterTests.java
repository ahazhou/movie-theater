package com.jpmc.theater;

import com.jpmc.theater.converter.WsShowingConverter;
import com.jpmc.theater.models.Customer;
import com.jpmc.theater.models.Movie;
import com.jpmc.theater.models.Reservation;
import com.jpmc.theater.models.Showing;
import com.jpmc.theater.utils.TheaterUtil;
import com.jpmc.theater.validators.ReservationValidator;
import com.jpmc.theater.validators.TheaterValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class TheaterTests {
    private static final int SEQUENCE = 1;
    private static final int TICKET_COUNT = 2;
    private static final String MOVIE_TITLE_1 = "movie title 1";
    private static final String MOVIE_TITLE_2 = "movie title 2";
    private static final int DURATION_1  = 3;
    private static final int DURATION_2 = 4;
    private static final String TICKET_PRICE_1 = "5";
    private static final String TICKET_PRICE_2 = "6";
    private static final int SPECIAL_CODE_1 = 7;
    private static final int SPECIAL_CODE_2 = 8;
    private static final int TIME_1 = 9;
    private static final int TIME_2 = 10;
    private static final int TIME_3 = 11;

    @Mock
    private Customer customer;
    @Mock
    private WsShowingConverter wsShowingConverter;
    @Mock
    private TheaterValidator theaterValidator;
    @Mock
    private ReservationValidator reservationValidator;
    @Mock
    private TheaterUtil theaterUtil;

    private Theater theater;
    private AutoCloseable autoCloseable;
    private List<Showing> schedule;
    @BeforeEach
    public void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        theater = new Theater(wsShowingConverter, theaterValidator, reservationValidator, theaterUtil);

        final Movie testMovie1 = new Movie(MOVIE_TITLE_1, Duration.ofMinutes(DURATION_1), new BigDecimal(TICKET_PRICE_1), SPECIAL_CODE_1);
        final Movie testMovie2 = new Movie(MOVIE_TITLE_2, Duration.ofMinutes(DURATION_2), new BigDecimal(TICKET_PRICE_2), SPECIAL_CODE_2);
        schedule = List.of(
                new Showing(testMovie1, LocalTime.of(TIME_1, TIME_2)),
                new Showing(testMovie2, LocalTime.of(TIME_3, TIME_2)),
                new Showing(testMovie1, LocalTime.of(TIME_2, TIME_3)));
        theater.setSchedule(schedule);
    }

    @AfterEach
    public void cleanUp() throws Exception {
        autoCloseable.close();
        verifyNoMoreInteractions(customer);
        verifyNoMoreInteractions(wsShowingConverter);
        verifyNoMoreInteractions(theaterValidator);
        verifyNoMoreInteractions(reservationValidator);
        verifyNoMoreInteractions(theaterUtil);
    }

    @Test
    void testReserve() {
        final Reservation result = theater.reserve(customer, SEQUENCE, TICKET_COUNT);
        verify(reservationValidator).validateTicketCount(TICKET_COUNT);
        verify(theaterValidator).validateExistingShowing(SEQUENCE, schedule);

        final Reservation expected = new Reservation(customer, schedule.get(SEQUENCE - 1), TICKET_COUNT);
        assertEquals(expected, result);
    }
}
