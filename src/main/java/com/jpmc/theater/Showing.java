package com.jpmc.theater;

import com.jpmc.theater.model.Movie;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Showing {
    private Movie movie;
    private int sequenceOfTheDay;
    private LocalDateTime startTime;

    public Showing(Movie movie, int sequenceOfTheDay, LocalDateTime startTime) {
        this.movie = movie;
        this.sequenceOfTheDay = sequenceOfTheDay;
        this.startTime = startTime;
    }

    public boolean isSequence(int sequence) {
        return this.sequenceOfTheDay == sequence;
    }

    public double getMovieOriginalPrice() {
        return movie.getTicketPrice();
    }

    public double getShowingPrice() {
        return getPriceCalculator().calculate(this);
    }

    PriceCalculator getPriceCalculator() {
        return PriceCalculator.singleton();
    }
}
