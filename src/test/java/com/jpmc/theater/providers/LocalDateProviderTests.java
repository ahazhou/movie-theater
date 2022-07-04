package com.jpmc.theater.providers;

import com.jpmc.theater.providers.LocalDateProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalDateProviderTests {
    @Test
    void testGetCurrentDate() {
        Assertions.assertDoesNotThrow(LocalDateProvider::currentDate);
    }
}
