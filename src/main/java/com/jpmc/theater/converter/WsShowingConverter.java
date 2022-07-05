package com.jpmc.theater.converter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jpmc.theater.managers.MovieManager;
import com.jpmc.theater.models.Showing;
import com.jpmc.theater.utils.TheaterUtil;
import com.jpmc.theater.ws.WsShowing;
import lombok.AllArgsConstructor;


@AllArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class WsShowingConverter {
    private final static String PRICE_FORMAT = "$%s";

    final MovieManager movieManager;
    final TheaterUtil theaterUtil;

    /**
     * Converts a showing into an object that can be returned as JSON.
     * @param showing The showing data to be formatted as JSON.
     * @return The object with formatted data that will be returned as JSON.
     */
    public WsShowing convertToWs(final Showing showing) {
        final WsShowing wsShowing = new WsShowing();
        wsShowing.setTitle(showing.getMovieTitle());
        wsShowing.setSequenceOfTheDay(showing.getSequenceOfTheDay());
        wsShowing.setShowStartTime(showing.getShowStartTime().toString());
        wsShowing.setRunningTime(theaterUtil.convertDurationToReadableFormat(showing.getMovieRunningTime()));
        wsShowing.setTicketPrice(String.format(PRICE_FORMAT, theaterUtil.roundHalfUp(showing.getMovieTicketPrice()).toString()));
        return wsShowing;
    }
}
