package com.jpmc.theater;

import com.jpmc.theater.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationTests {
    @Mock
    private Customer customer;
    @Mock
    private Showing showing;

    @Test
    void totalFee() {
        double singleTicketPrice = 10.0;
        int audienceCount = 3;
        when(showing.getShowingPrice()).thenReturn(singleTicketPrice);
        double totalFee = new Reservation(customer, showing, audienceCount).totalFee();
        assertThat(totalFee).isEqualTo(singleTicketPrice * audienceCount);
    }
}
