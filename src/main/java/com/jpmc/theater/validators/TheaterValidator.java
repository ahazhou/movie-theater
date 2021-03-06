package com.jpmc.theater.validators;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jpmc.theater.models.Showing;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.util.List;

@Singleton
@NoArgsConstructor(onConstructor_ = @Inject)
public class TheaterValidator {
    final Logger logger = Logger.getLogger(TheaterValidator.class);

    /**
     * Validates if the existing showing exists based on a given id.
     *
     * @param sequence The id to find an existing showing in the provided schedule.
     * @param schedule The list of showings
     */
    public void validateExistingShowing(final int sequence, final List<Showing> schedule) {
        if(sequence < 0 || sequence >= schedule.size()) {
            throw new IllegalStateException(String.format("Unable to find showing for sequence (%s)", sequence));
        }
        logger.debug(String.format("Successfully validated sequence (%s) for showing.", sequence));
    }
}
