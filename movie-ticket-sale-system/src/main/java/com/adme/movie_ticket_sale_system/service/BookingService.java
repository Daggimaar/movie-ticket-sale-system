package com.adme.movie_ticket_sale_system.service;

import com.adme.movie_ticket_sale_system.entity.Booking;
import com.adme.movie_ticket_sale_system.entity.Movie;

import java.util.List;
import java.util.Set;

public interface BookingService {

    Booking findById(Long id);
    List<Booking> findAll();
    void delete(Booking booking);
    void bookSeats(Integer showingId, List<String> seatNumbers, String email);
    void bookSeatsWithValidation(Integer showingId, String seatLabels, String email);
    Set<String> getBooking(Integer showingId);

}
