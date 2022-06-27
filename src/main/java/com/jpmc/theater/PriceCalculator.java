package com.jpmc.theater;

import com.jpmc.theater.model.Movie;
import com.jpmc.theater.util.LocalDateProvider;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

public class PriceCalculator {
    private static int MOVIE_CODE_SPECIAL = 1;
    private static PriceCalculator instance = null;

    /**
     * @return make sure to return singleton instance
     */
    public static PriceCalculator singleton() {
        if (instance == null) {
            instance = new PriceCalculator();
        }
        return instance;
    }

    public double calculate(Showing showing) {
        return showing.getMovie().getTicketPrice() - getDiscount(showing);
    }

    double getDiscount(Showing showing) {
        return Stream.of(specialDiscount(showing),
                        sequenceDiscount(showing),
                        dayDiscount(),
                        timeDiscount(showing))
                .max(Double::compare).orElse(0.0);
    }

    double specialDiscount(Showing showing) {
        double discount = 0;
        Movie movie = showing.getMovie();
        if (MOVIE_CODE_SPECIAL == movie.getSpecialCode()) {
            discount = movie.getTicketPrice() * 0.2;
        }
        return discount;
    }

    double sequenceDiscount(Showing showing) {
        double discount = 0;

        if (showing.isSequence(1)) {
            discount = 3; // $3 discount for 1st show
        } else if (showing.isSequence(2)) {
            discount = 2; // $2 discount for 2nd show
        }
        return discount;
    }

    double dayDiscount() {
        int date = getLocalDateProvider().currentDate().getDayOfMonth();
        double discount = 0;
        if (date == 7) {
            discount = 1; // $1 discount for 7th each month
        }
        return discount;
    }

    double timeDiscount(Showing showing) {
        double discount = 0;
        LocalDateTime startTime = showing.getStartTime();
        LocalDateTime eleven = LocalDateTime.of(getLocalDateProvider().currentDate(), LocalTime.of(11, 0));
        LocalDateTime sixteen = LocalDateTime.of(getLocalDateProvider().currentDate(), LocalTime.of(16, 0));
        if (startTime.isEqual(eleven)
                || startTime.isEqual(sixteen)
                || (startTime.isAfter(eleven) && startTime.isBefore(sixteen))) {
            discount = showing.getMovie().getTicketPrice() * 0.25;
        }
        return discount;
    }

    LocalDateProvider getLocalDateProvider() {
        return LocalDateProvider.singleton();
    }
}
