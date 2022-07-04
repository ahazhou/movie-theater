package com.jpmc.theater.managers;

import com.jpmc.theater.models.Movie;
import com.jpmc.theater.models.Showing;
import com.jpmc.theater.utils.DiscountUtil;
import com.jpmc.theater.utils.TheaterUtil;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MovieManagerTests {
    private static final String MOVIE_TITLE = "Spider-Man: No Way Home";
    private static final Duration MOVIE_LENGTH = Duration.ofMinutes(90L);
    private static final BigDecimal TICKET_PRICE = new BigDecimal("12.50");
    private static final BigDecimal DISCOUNT_AMOUNT = new BigDecimal("2.76");
    private static final int SPECIAL_CODE = 1;

    @Mock
    private DiscountUtil discountUtil;
    @Mock
    private TheaterUtil theaterUtil;
    private MovieManager movieManager;
    private AutoCloseable autoCloseable;

    @BeforeEach
    public void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        movieManager = new MovieManager(discountUtil, theaterUtil);
    }

    @AfterEach
    public void cleanUp() throws Exception {
        autoCloseable.close();
        verifyNoMoreInteractions(discountUtil);
    }

    @Test
    public void testCalculateTicketPrice() {
        final Movie spiderMan = new Movie(MOVIE_TITLE, MOVIE_LENGTH, TICKET_PRICE, SPECIAL_CODE);
        final Showing showing = new Showing(spiderMan, LocalTime.now());

        final BigDecimal expectedResult = new BigDecimal("9.74");

        when(discountUtil.getDiscount(spiderMan, showing)).thenReturn(DISCOUNT_AMOUNT);
        when(theaterUtil.roundHalfUp(expectedResult)).thenReturn(expectedResult);

        final BigDecimal result = movieManager.calculateTicketPrice(spiderMan, showing);

        assertEquals(expectedResult, result);

        verify(discountUtil).getDiscount(spiderMan, showing);
    }

    @Test
    public void testCalculateTicketPrice_noDiscount() {
        final Movie spiderMan = new Movie(MOVIE_TITLE, MOVIE_LENGTH, TICKET_PRICE, SPECIAL_CODE);
        final Showing showing = new Showing(spiderMan, LocalTime.now());

        when(discountUtil.getDiscount(spiderMan, showing)).thenReturn(BigDecimal.ZERO);
        when(theaterUtil.roundHalfUp(TICKET_PRICE)).thenReturn(TICKET_PRICE);

        final BigDecimal result = movieManager.calculateTicketPrice(spiderMan, showing);

        assertEquals(TICKET_PRICE, result);

        verify(discountUtil).getDiscount(spiderMan, showing);
    }
}
