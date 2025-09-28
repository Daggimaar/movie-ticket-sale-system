package com.adme.movie_ticket_sale_system.repository;

import com.adme.movie_ticket_sale_system.entity.Booking;
import com.adme.movie_ticket_sale_system.entity.Showing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByShowing(Showing showing);
    void deleteByShowing(Showing showing);
}