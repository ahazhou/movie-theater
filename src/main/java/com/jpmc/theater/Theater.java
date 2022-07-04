package com.jpmc.theater;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.jpmc.theater.converter.WsShowingConverter;
import com.jpmc.theater.models.Customer;
import com.jpmc.theater.models.Movie;
import com.jpmc.theater.models.Reservation;
import com.jpmc.theater.models.Showing;
import com.jpmc.theater.providers.LocalDateProvider;
import com.jpmc.theater.utils.TheaterUtil;
import com.jpmc.theater.validators.ReservationValidator;
import com.jpmc.theater.validators.TheaterValidator;
import com.jpmc.theater.ws.WsShowing;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@EqualsAndHashCode
@ToString
public class Theater {
    final Logger logger = Logger.getLogger(Theater.class);

    private final static String SCHEDULE_FORMAT = "%s: %s %s %s $%s";

    @Setter
    private List<Showing> schedule;
    private final WsShowingConverter wsShowingConverter;
    private final TheaterValidator theaterValidator;
    private final ReservationValidator reservationValidator;
    private final TheaterUtil theaterUtil;

    @VisibleForTesting
    void createShowings() {
        //Note: Ideally we have a CSV parser to read in movies and showings. However, it might be out of scope
        //for this project. To expand on this project, we should have a db that collects these values as well.
        logger.debug("Creating movies for theater.");
        final Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(12.5), 1);
        final Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), BigDecimal.valueOf(11), 0);
        final Movie theBatMan = new Movie("The Batman", Duration.ofMinutes(95), BigDecimal.valueOf(9), 0);
        logger.debug("Creating schedule for theater.");
        schedule = List.of(
                new Showing(turningRed, LocalTime.of(9, 0)),
                new Showing(spiderMan, LocalTime.of(11, 0)),
                new Showing(theBatMan, LocalTime.of(12, 50)),
                new Showing(turningRed, LocalTime.of(14, 30)),
                new Showing(spiderMan, LocalTime.of(16, 10)),
                new Showing(theBatMan, LocalTime.of(17, 50)),
                new Showing(turningRed, LocalTime.of(19, 30)),
                new Showing(spiderMan, LocalTime.of(21, 10)),
                new Showing(theBatMan, LocalTime.of(23, 0))
        );
        logger.info(String.format("Created schedule of %s showings", schedule.size()));
    }

    /**
     * Creates a new reservation for a customer while validating the input.
     *
     * @param customer The customer who wants to create a new reservation.
     * @param sequence Which showing in the daily schedule the customer wants to make a reservation.
     * @param howManyTickets How many tickets the customer wants to purchase.
     * @return The created reservation.
     */
    public Reservation reserve(final Customer customer, final int sequence, final int howManyTickets) {
        reservationValidator.validateTicketCount(howManyTickets);
        theaterValidator.validateExistingShowing(sequence, schedule);
        logger.debug("Successfully validated reservation details");
        return new Reservation(customer, schedule.get(sequence - 1), howManyTickets);
    }

    /**
     * Prints the daily schedule for the theater.
     */
    public void printSchedule() {
        System.out.println(LocalDateProvider.currentDate());
        System.out.println("Schedule in Simple Format");
        System.out.println("===================================================");
        schedule.forEach(this::formatAndPrintShowing);
        System.out.println("===================================================");
        System.out.println("Schedule in JSON Format");
        System.out.println("===================================================");
        schedule.forEach(this::formatAndPrintShowingInJson);
        System.out.println("===================================================");

    }

    private void formatAndPrintShowing(final Showing showing) {
        logger.debug(String.format("Attempting to format showing for sequence (%s) for print", showing.getSequenceOfTheDay()));
        final String formattedShowing = String.format(
                SCHEDULE_FORMAT,
                showing.getSequenceOfTheDay(),
                showing.getShowStartTime(),
                showing.getMovieTitle(),
                theaterUtil.convertDurationToReadableFormat(showing.getMovieRunningTime()),
                showing.getMovieTicketPrice());
        System.out.println(formattedShowing);
    }

    private void formatAndPrintShowingInJson(final Showing showing) {
        logger.debug(String.format("Attempting to format showing for sequence (%s) into JSON for print", showing.getSequenceOfTheDay()));
        ObjectMapper mapper = new ObjectMapper();
        try {
            final WsShowing wsShowing = wsShowingConverter.convertToWs(showing);
            String showingJson = mapper.writeValueAsString(wsShowing);
            System.out.println(showingJson);
        }
        catch (JsonProcessingException e) {
            logger.error(String.format("Unable to convert Showing with sequence (%s) into JSON with the following error: %s", showing.getSequenceOfTheDay(), e));
        }
    }


    public static void main(String[] args) {
        final Injector injector = Guice.createInjector();
        final Theater theater = injector.getInstance(Theater.class);
        theater.createShowings();
        theater.printSchedule();
    }
}
