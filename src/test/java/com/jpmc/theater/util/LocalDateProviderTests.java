package com.jpmc.theater.util;


import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateProviderTests {
    @Test
    void makeSureCurrentTime() {
        LocalDate actual = LocalDateProvider.singleton().currentDate();
        assertThat(actual).isToday();
    }

    @Test
    void testSingleton() {
        LocalDateProvider first = LocalDateProvider.singleton();
        LocalDateProvider second = LocalDateProvider.singleton();
        assertThat(first).isEqualTo(second);
    }
}
