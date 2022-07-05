package com.jpmc.theater.providers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Singleton
@NoArgsConstructor(onConstructor_ = @Inject)
public class LocalDateProvider {
    //Note: although this class only returns today's date, we keep this just in case we
    //might need to change how we define the current date.
    public static LocalDate currentDate() {
            return LocalDate.now();
    }
}
