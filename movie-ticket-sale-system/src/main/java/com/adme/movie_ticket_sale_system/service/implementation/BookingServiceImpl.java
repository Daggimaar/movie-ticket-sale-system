package com.adme.movie_ticket_sale_system.service.implementation;

import com.adme.movie_ticket_sale_system.entity.Booking;
import com.adme.movie_ticket_sale_system.entity.Showing;
import com.adme.movie_ticket_sale_system.repository.BookingRepository;
import com.adme.movie_ticket_sale_system.repository.ShowingRepository;
import com.adme.movie_ticket_sale_system.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ShowingRepository showingRepository;
    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ShowingRepository showingRepository) {
        this.bookingRepository = bookingRepository;
        this.showingRepository = showingRepository;
    }

    @Override
    public void bookSeats(Integer showingId, List<String> seatNumbers, String email) {
        Showing showing = showingRepository.findById(showingId)
                .orElseThrow(() -> new RuntimeException("Showing not found"));

        if (showing.getAvailableSeats() < seatNumbers.size()) {
            throw new RuntimeException("Not enough seats available!");
        }

        // Already booked seats
        Set<String> alreadyBooked = getBooking(showingId);

        for (String seat : seatNumbers) {
            if (alreadyBooked.contains(seat)) {
                throw new RuntimeException("Seat " + seat + " is already booked!");
            }
        }

        // Save booking
        for (String seat : seatNumbers) {
            Booking booking = new Booking();
            booking.setSeatNumber(seat);
            booking.setShowing(showing);
            booking.setEmail(email);
            bookingRepository.save(booking);
        }

        // Update available seat count
        showing.setAvailableSeats(showing.getAvailableSeats() - seatNumbers.size());
        showingRepository.save(showing);
    }

    @Override
    public void bookSeatsWithValidation(Integer showingId, String selectedSeats, String email) {
        if (selectedSeats == null || selectedSeats.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select at least one seat.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter your email.");
        }

        List<String> seatLabels = Arrays.asList(selectedSeats.split(","));
        bookSeats(showingId, seatLabels, email);
    }


    @Override
    public Set<String> getBooking(Integer showingId) {
        Showing showing = showingRepository.findById(showingId)
                .orElseThrow(() -> new RuntimeException("Showing not found"));

        return bookingRepository.findByShowing(showing)
                .stream()
                .map(Booking::getSeatNumber)
                .collect(Collectors.toSet());
    }
    @Override
    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
    }
    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public void delete(Booking booking) {
        bookingRepository.delete(booking);
    }

}

