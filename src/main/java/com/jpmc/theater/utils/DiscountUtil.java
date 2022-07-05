package com.jpmc.theater.utils;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Suppliers;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jpmc.theater.models.Movie;
import com.jpmc.theater.models.Showing;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Supplier;

@Singleton
@NoArgsConstructor(onConstructor_ = @Inject)
public class DiscountUtil {
    final Logger logger = Logger.getLogger(DiscountUtil.class);

    //Note: We lazy load the BigDecimal values to prevent from wasting resources if the BigDecimal is not used.
    @VisibleForTesting
    static final Supplier<BigDecimal> MOVIE_CODE_SPECIAL_DISCOUNT_SUPPLIER = Suppliers.memoize(DiscountUtil::movieCodeSpecialDiscountSupplier);
    @VisibleForTesting
    static final Supplier<BigDecimal> FIRST_SHOW_DISCOUNT_SUPPLIER = Suppliers.memoize(DiscountUtil::firstShowDiscountSupplier);
    @VisibleForTesting
    static final Supplier<BigDecimal> SECOND_SHOW_DISCOUNT_SUPPLIER = Suppliers.memoize(DiscountUtil::secondShowDiscountSupplier);
    @VisibleForTesting
    static final Supplier<BigDecimal> SHOW_START_TIME_DISCOUNT_SUPPLIER = Suppliers.memoize(DiscountUtil::showStartTimeDiscountSupplier);
    @VisibleForTesting
    static final Supplier<BigDecimal> SPECIAL_DAY_OF_THE_MONTH_DISCOUNT_SUPPLIER = Suppliers.memoize(DiscountUtil::specialDayOfTheMonthSupplier);

    private static final int SHOW_START_TIME_DISCOUNT_BEGINNING_TIME = 11;
    private static final int SHOW_START_TIME_DISCOUNT_ENDING_TIME = 16;
    private static final int SHOW_SPECIAL_DAY_OF_THE_MONTH = 7;

    private static BigDecimal movieCodeSpecialDiscountSupplier() {
        //The special movie discount is 20% off
        return new BigDecimal("0.20");
    }

    private static BigDecimal firstShowDiscountSupplier() {
        //The first show discount is $3
        return new BigDecimal("3");
    }

    private static BigDecimal secondShowDiscountSupplier() {
        //The second show discount is $2
        return new BigDecimal("2");
    }

    private static BigDecimal showStartTimeDiscountSupplier() {
        //If the movie starts between 11AM ~ 4PM, there's a 25% discount
        return new BigDecimal("0.25");
    }

    private static BigDecimal specialDayOfTheMonthSupplier() {
        //If the movie shows on the 7th, there's a $1 discount
        return new BigDecimal("1");
    }

    /**
     * Calculates how much discount a ticket price should have based on the movie and showing parameters.
     *
     * @param movie The movie details to calculate the discount.
     * @param showing The showing details to calculate the discount.
     * @return The total discount that should be taken off of the original movie ticket price.
     */
    public BigDecimal getDiscount(final Movie movie, final Showing showing) {
        return getSpecialMovieDiscount(movie)
                .max(getFirstOrSecondShowDiscount(showing.getSequenceOfTheDay()))
                .max(getMiddleOfTheDayDiscount(movie, showing))
                .max(getSpecialDayOfTheMonthDiscount(showing));
    }

    private BigDecimal getSpecialMovieDiscount(final Movie movie) {
        logger.debug("Calculating special movie discount");
        if (Movie.MOVIE_CODE_SPECIAL == movie.getSpecialCode()) {
            logger.debug("Special movie discount is applicable");
            return movie.getTicketPrice().multiply(MOVIE_CODE_SPECIAL_DISCOUNT_SUPPLIER.get());
        }
        logger.debug("Special movie discount is not applicable");
        return BigDecimal.ZERO;
    }

    private BigDecimal getFirstOrSecondShowDiscount(final int showSequence) {
        logger.debug("Calculating first or second show discount");
        switch (showSequence) {
            case 1:
                logger.debug("First show discount is applicable");
                return FIRST_SHOW_DISCOUNT_SUPPLIER.get();
            case 2:
                logger.debug("Second show discount is applicable");
                return SECOND_SHOW_DISCOUNT_SUPPLIER.get();
            default:
                logger.debug("First and second show discount is not applicable");
                return BigDecimal.ZERO;
        }
    }

    private BigDecimal getMiddleOfTheDayDiscount(final Movie movie, final Showing showing) {
        logger.debug("Calculating middle of the day discount");
        final LocalTime showStartTime = showing.getShowStartTime().toLocalTime();
        if(showStartTime.getHour() >= SHOW_START_TIME_DISCOUNT_BEGINNING_TIME && showStartTime.getHour() <= SHOW_START_TIME_DISCOUNT_ENDING_TIME) {
            logger.debug("Middle of the day discount is applicable");
            return movie.getTicketPrice().multiply(SHOW_START_TIME_DISCOUNT_SUPPLIER.get());
        }
        logger.debug("Middle of the day discount is not applicable");
        return BigDecimal.ZERO;
    }

    private BigDecimal getSpecialDayOfTheMonthDiscount(final Showing showing) {
        logger.debug("Calculating special day of the month discount");
        final LocalDate showDate = showing.getShowStartTime().toLocalDate();
        if(showDate.getDayOfMonth() == SHOW_SPECIAL_DAY_OF_THE_MONTH) {
            logger.debug("Special day of the month discount is applicable");
            return SPECIAL_DAY_OF_THE_MONTH_DISCOUNT_SUPPLIER.get();
        }
        logger.debug("Special day of the month discount is not applicable");
        return BigDecimal.ZERO;
    }
}
