package com.jpmc.theater.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationValidatorTest {

    private ReservationValidator reservationValidator;
    @BeforeEach
    public void init() {
        reservationValidator = new ReservationValidator();
    }

    @Test
    public void testValidateTicketCount_normal() {
        Assertions.assertDoesNotThrow(() -> reservationValidator.validateTicketCount(3));
    }

    @Test
    public void testValidateTicketCount_negative() {
        Assertions.assertThrows(IllegalStateException.class, () -> reservationValidator.validateTicketCount(-1));
    }
}
