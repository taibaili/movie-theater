package com.jpmc.theater;

import com.jpmc.theater.model.Movie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowingTest {
    @Mock
    private Movie movie;

    @Test
    void isSequence() {
        Showing showing = new Showing(movie, 1, LocalDateTime.now());
        assertTrue(showing.isSequence(1));
        assertFalse(showing.isSequence(2));
    }

    @Test
    void getMovieOriginalPrice() {
        when(movie.getTicketPrice()).thenReturn(5.6);
        Showing showing = new Showing(movie, 1, LocalDateTime.now());

        assertThat(showing.getMovieOriginalPrice()).isEqualTo(5.6);
    }

    @Test
    void getShowingPrice() {
        Showing showing = spy(new Showing(movie, 1, LocalDateTime.now()));
        PriceCalculator priceCalculator = mock(PriceCalculator.class);
        when(priceCalculator.calculate(showing)).thenReturn(5.0);
        when(showing.getPriceCalculator()).thenReturn(priceCalculator);
        assertThat(showing.getShowingPrice()).isEqualTo(5.0);
    }
}