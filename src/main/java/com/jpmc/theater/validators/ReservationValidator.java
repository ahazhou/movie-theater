package com.jpmc.theater.validators;

import org.apache.log4j.Logger;

import javax.inject.Singleton;

@Singleton
public class ReservationValidator {
    final Logger logger = Logger.getLogger(ReservationValidator.class);

    /**
     * Validates that the number of tickets in a reservation is not negative.
     * @param howManyTickets The number of tickets in a reservation.
     */
    public void validateTicketCount(final int howManyTickets) {
        logger.debug("Validating reservation ticket count");
        if(howManyTickets < 0) {
            throw new IllegalStateException("Reservation's ticket count cannot be negative.");
        }
    }
}
