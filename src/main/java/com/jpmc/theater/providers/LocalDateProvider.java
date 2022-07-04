package com.jpmc.theater.providers;

import javax.inject.Singleton;
import java.time.LocalDate;

@Singleton
public class LocalDateProvider {
    //Note: although this class only returns today's date, we keep this just in case we
    //might need to change how we define the current date.
    public static LocalDate currentDate() {
            return LocalDate.now();
    }
}
