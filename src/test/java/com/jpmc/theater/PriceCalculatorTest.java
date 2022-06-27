package com.jpmc.theater;

import com.jpmc.theater.model.Movie;
import com.jpmc.theater.util.LocalDateProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceCalculatorTest {
    @Mock
    private Showing showing;
    private final double moviePrice = 10.0;

    @Test
    void singleton() {
        PriceCalculator pc1 = PriceCalculator.singleton();
        PriceCalculator pc2 = PriceCalculator.singleton();
        assertThat(pc1).isEqualTo(pc2);
    }

    @Test
    void calculate() {
        Movie movie = mock(Movie.class);
        when(movie.getTicketPrice()).thenReturn(moviePrice);
        when(showing.getMovie()).thenReturn(movie);

        PriceCalculator priceCalculator = spy(PriceCalculator.singleton());
        doReturn(2.0).when(priceCalculator).getDiscount(showing);
        assertThat(priceCalculator.calculate(showing)).isEqualTo(moviePrice - 2.0);
    }

    @Test
    void getDiscount() {
        PriceCalculator priceCalculator = spy(PriceCalculator.singleton());

        doReturn(1.0).when(priceCalculator).specialDiscount(showing);
        doReturn(2.0).when(priceCalculator).dayDiscount();
        doReturn(3.0).when(priceCalculator).sequenceDiscount(showing);
        doReturn(4.0).when(priceCalculator).timeDiscount(showing);

        assertThat(priceCalculator.getDiscount(showing)).isEqualTo(4.0);
    }

    @Test
    void specialDiscount_valid() {
        Movie movie = mock(Movie.class);
        when(movie.getSpecialCode()).thenReturn(1);
        when(movie.getTicketPrice()).thenReturn(moviePrice);
        when(showing.getMovie()).thenReturn(movie);

        double discount = PriceCalculator.singleton().specialDiscount(showing);
        assertThat(discount).isEqualTo(moviePrice * 0.2);
    }

    @Test
    void specialDiscount_invalid() {
        Movie movie = mock(Movie.class);
        when(movie.getSpecialCode()).thenReturn(2);
        when(showing.getMovie()).thenReturn(movie);

        double discount = PriceCalculator.singleton().specialDiscount(showing);
        assertThat(discount).isEqualTo(0);
    }

    @Test
    void sequenceDiscount_isSequence1() {
        when(showing.isSequence(1)).thenReturn(true);
        double discount = PriceCalculator.singleton().sequenceDiscount(showing);
        assertThat(discount).isEqualTo(3);
    }

    @Test
    void sequenceDiscount_isSequence2() {
        when(showing.isSequence(1)).thenReturn(false);
        when(showing.isSequence(2)).thenReturn(true);

        double discount = PriceCalculator.singleton().sequenceDiscount(showing);
        assertThat(discount).isEqualTo(2);
    }

    @Test
    void sequenceDiscount_isSequenceOther() {
        when(showing.isSequence(1)).thenReturn(false);
        when(showing.isSequence(2)).thenReturn(false);

        double discount = PriceCalculator.singleton().sequenceDiscount(showing);
        assertThat(discount).isEqualTo(0);
    }

    @Test
    void dayDiscount_is7th() {
        PriceCalculator priceCalculator = spy(PriceCalculator.singleton());
        LocalDateProvider localDateProvider = mock(LocalDateProvider.class);
        when(localDateProvider.currentDate()).thenReturn(LocalDate.of(2022, 9, 7));
        when(priceCalculator.getLocalDateProvider()).thenReturn(localDateProvider);

        double discount = priceCalculator.dayDiscount();
        assertThat(discount).isEqualTo(1);
    }

    @Test
    void timeDiscount_between11And16() {
        Movie movie = mock(Movie.class);
        when(movie.getTicketPrice()).thenReturn(moviePrice);
        PriceCalculator priceCalculator = PriceCalculator.singleton();
        when(showing.getStartTime()).thenReturn(LocalDateTime.of(priceCalculator.getLocalDateProvider().currentDate(),
                LocalTime.of(12, 32)));
        when(showing.getMovie()).thenReturn(movie);

        double discount = PriceCalculator.singleton().timeDiscount(showing);
        assertThat(discount).isEqualTo(moviePrice * 0.25);
    }

    @Test
    void timeDiscount_on11() {
        Movie movie = mock(Movie.class);
        when(movie.getTicketPrice()).thenReturn(moviePrice);
        PriceCalculator priceCalculator = PriceCalculator.singleton();
        when(showing.getStartTime()).thenReturn(LocalDateTime.of(priceCalculator.getLocalDateProvider().currentDate(),
                LocalTime.of(11, 01)));
        when(showing.getMovie()).thenReturn(movie);

        double discount = PriceCalculator.singleton().timeDiscount(showing);
        assertThat(discount).isEqualTo(moviePrice * 0.25);
    }

    @Test
    void timeDiscount_before11() {
        PriceCalculator priceCalculator = PriceCalculator.singleton();
        when(showing.getStartTime()).thenReturn(LocalDateTime.of(priceCalculator.getLocalDateProvider().currentDate(),
                LocalTime.of(10, 59)));

        double discount = PriceCalculator.singleton().timeDiscount(showing);
        assertThat(discount).isEqualTo(0);
    }
}