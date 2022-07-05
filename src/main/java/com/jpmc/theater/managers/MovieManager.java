package com.jpmc.theater.managers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jpmc.theater.models.Movie;
import com.jpmc.theater.models.Showing;
import com.jpmc.theater.utils.DiscountUtil;
import com.jpmc.theater.utils.TheaterUtil;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class MovieManager {
    final Logger logger = Logger.getLogger(MovieManager.class);

    final DiscountUtil discountUtil;
    final TheaterUtil theaterUtil;

    /**
     * Calculates and returns the ticket price for a given movie after discounts. Will round to the closest cent.
     *
     * @param movie The movie whose ticket price we want to determine.
     * @param showing The showing information to calculate the discount.
     * @return The final ticket price.
     */
    public BigDecimal calculateTicketPrice(final Movie movie, final Showing showing) {
        logger.debug(String.format("Calculating the ticket price for showing with sequence number: (%s)", showing.getSequenceOfTheDay()));
        return theaterUtil.roundHalfUp(movie.getTicketPrice().subtract(discountUtil.getDiscount(movie, showing)));
    }



    /**
     * Retrieves the ticket price of the movie without discount. Will round to the closest cent.
     *
     * @param movie The movie whose ticket price will be returned.
     * @return The price of the ticket of the movie without discounts.
     */
    public BigDecimal getMovieFee(final Movie movie) {
        logger.debug("Rounding the ticket price to the nearest cent");
        return theaterUtil.roundHalfUp(movie.getTicketPrice());
    }
}
