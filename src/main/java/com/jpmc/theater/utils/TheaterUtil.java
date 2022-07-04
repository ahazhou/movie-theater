package com.jpmc.theater.utils;

import com.google.common.annotations.VisibleForTesting;
import org.apache.log4j.Logger;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Singleton
public class TheaterUtil {
    final Logger logger = Logger.getLogger(TheaterUtil.class);

    @VisibleForTesting
    public final static String MOVIE_RUNNING_TIME_FORMAT = "(%s hour%s %s minute%s)";

    /**
     * Limits the BigDecimal to two decimal places by rounding to the closest hundredth.
     * This effectively acts to convert a given number to a money value.
     *
     * @param bigDecimal The value which will be rounded.
     * @return The rounded result.
     */
    public BigDecimal roundHalfUp(final BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Converts the given duration into a readable format.
     *
     * @param duration The value to be converted into a readable format.
     * @return A readable format based off of the given duration.
     */
    public String convertDurationToReadableFormat(Duration duration) {
        logger.debug("Attempting to format duration to readable format.");
        long hour = duration.toHours();
        long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());

        return String.format(MOVIE_RUNNING_TIME_FORMAT, hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
    }

    // (s) postfix should be added to handle plural correctly
    private String handlePlural(long value) {
        return value == 1 ? "" : "s";
    }
}
