package com.jpmc.theater.validators;

import com.jpmc.theater.models.Showing;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.verifyNoMoreInteractions;

public class TheaterValidatorTest {
    @Mock
    private Showing showing;
    private TheaterValidator theaterValidator;

    private AutoCloseable autoCloseable;
    private List<Showing> showingList;
    @BeforeEach
    public void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        theaterValidator = new TheaterValidator();
        showingList = List.of(showing, showing, showing);

    }

    @AfterEach
    public void cleanUp() throws Exception {
        autoCloseable.close();
        verifyNoMoreInteractions(showing);
    }

    @Test
    public void testValidateExistingShowing_successful() {
        Assertions.assertDoesNotThrow(() -> theaterValidator.validateExistingShowing(showingList.size() - 1, showingList));
    }

    @Test
    public void testValidateExistingShowing_negativeSequence() {
        Assertions.assertThrows(IllegalStateException.class, () -> theaterValidator.validateExistingShowing(-1, showingList));
    }

    @Test
    public void testValidateExistingShowing_tooBigSequence() {
        Assertions.assertThrows(IllegalStateException.class, () -> theaterValidator.validateExistingShowing(showingList.size() + 1, showingList));
    }
}
