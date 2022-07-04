package com.jpmc.theater.utils;

import com.jpmc.theater.models.Movie;
import com.jpmc.theater.models.Showing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountUtilTest {
    private static final String MOVIE_TITLE = "Spider-Man: No Way Home";
    private static final Duration MOVIE_LENGTH = Duration.ofMinutes(90L);
    private static final BigDecimal TICKET_PRICE = new BigDecimal("12.5");
    private static final int SPECIAL_CODE = 1;
    private static final int NON_SPECIAL_CODE = 2;
    private static final int FIRST_SHOWING = 1;
    private static final int SECOND_SHOWING = 2;
    private static final int THIRD_SHOWING = 3;
    private static final int YEAR = 2022;
    private static final int MONTH = 7;
    private static final int DAY = 4;
    private static final int SPECIAL_DAY = 7;
    private static final int HOUR = 7;
    private static final int SPECIAL_HOUR = 13;
    private static final int MINUTE = 13;
    private static final int SECOND = 15;

    private DiscountUtil discountUtil;
    private  LocalDateTime localDateTime;

    @BeforeEach
    public void init() {
        discountUtil = new DiscountUtil();
        localDateTime = LocalDateTime.of(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
    }

    @Test
    void testGetDiscount_specialMovieDiscount() {
        final Movie spiderMan = new Movie(MOVIE_TITLE, MOVIE_LENGTH, TICKET_PRICE, SPECIAL_CODE);
        final Showing showing = new Showing(spiderMan, THIRD_SHOWING, localDateTime);

        final BigDecimal result = discountUtil.getDiscount(spiderMan, showing);

        final BigDecimal expected = TICKET_PRICE.multiply(DiscountUtil.MOVIE_CODE_SPECIAL_DISCOUNT_SUPPLIER.get());
        assertEquals(expected, result);
    }

    @Test
    void testGetDiscount_firstShowDiscount() {
        final Movie spiderMan = new Movie(MOVIE_TITLE, MOVIE_LENGTH, TICKET_PRICE, NON_SPECIAL_CODE);
        final Showing showing = new Showing(spiderMan, FIRST_SHOWING, localDateTime);

        final BigDecimal result = discountUtil.getDiscount(spiderMan, showing);

        assertEquals(DiscountUtil.FIRST_SHOW_DISCOUNT_SUPPLIER.get(), result);
    }

    @Test
    void testGetDiscount_secondShowDiscount() {
        final Movie spiderMan = new Movie(MOVIE_TITLE, MOVIE_LENGTH, TICKET_PRICE, NON_SPECIAL_CODE);
        final Showing showing = new Showing(spiderMan, SECOND_SHOWING, localDateTime);

        final BigDecimal result = discountUtil.getDiscount(spiderMan, showing);

        assertEquals(DiscountUtil.SECOND_SHOW_DISCOUNT_SUPPLIER.get(), result);
    }

    @Test
    void testGetDiscount_middleOfTheDayDiscount() {
        final Movie spiderMan = new Movie(MOVIE_TITLE, MOVIE_LENGTH, TICKET_PRICE, NON_SPECIAL_CODE);
        localDateTime = LocalDateTime.of(YEAR, MONTH, DAY, SPECIAL_HOUR, MINUTE, SECOND);
        final Showing showing = new Showing(spiderMan, THIRD_SHOWING, localDateTime);

        final BigDecimal result = discountUtil.getDiscount(spiderMan, showing);

        final BigDecimal expected = TICKET_PRICE.multiply(DiscountUtil.SHOW_START_TIME_DISCOUNT_SUPPLIER.get());
        assertEquals(expected, result);
    }

    @Test
    void testGetDiscount_specialDayOfTheMonthDiscount() {
        final Movie spiderMan = new Movie(MOVIE_TITLE, MOVIE_LENGTH, TICKET_PRICE, NON_SPECIAL_CODE);
        localDateTime = LocalDateTime.of(YEAR, MONTH, SPECIAL_DAY, HOUR, MINUTE, SECOND);
        final Showing showing = new Showing(spiderMan, THIRD_SHOWING, localDateTime);

        final BigDecimal result = discountUtil.getDiscount(spiderMan, showing);

        assertEquals(DiscountUtil.SPECIAL_DAY_OF_THE_MONTH_DISCOUNT_SUPPLIER.get(), result);
    }

    @Test
    void testGetDiscount_noDiscount() {
        final Movie spiderMan = new Movie(MOVIE_TITLE, MOVIE_LENGTH, TICKET_PRICE, NON_SPECIAL_CODE);
        final Showing showing = new Showing(spiderMan, THIRD_SHOWING, localDateTime);

        final BigDecimal result = discountUtil.getDiscount(spiderMan, showing);

        assertEquals(BigDecimal.ZERO, result);
    }
}
