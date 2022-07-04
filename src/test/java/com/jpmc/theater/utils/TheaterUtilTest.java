package com.jpmc.theater.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TheaterUtilTest {
    private TheaterUtil theaterUtil;
    @BeforeEach
    public void init() {
        theaterUtil = new TheaterUtil();
    }

    @Test
    public void testRoundHalfUp_roundUp() {
        final BigDecimal randomNumber = new BigDecimal("0.117");
        final BigDecimal result = theaterUtil.roundHalfUp(randomNumber);

        assertEquals("0.12", result.toString());
    }

    @Test
    public void testRoundHalfUp_roundDown() {
        final BigDecimal randomNumber = new BigDecimal("0.1145");
        final BigDecimal result = theaterUtil.roundHalfUp(randomNumber);

        assertEquals("0.11", result.toString());
    }

    @Test
    public void testConvertDurationToReadableFormat() {
        final Duration duration = Duration.ofMinutes(100);
        long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());
        final String expected = String.format(TheaterUtil.MOVIE_RUNNING_TIME_FORMAT, duration.toHours(), "", remainingMin, "s");
        final String result = theaterUtil.convertDurationToReadableFormat(duration);
        assertEquals(expected, result);
    }
}
