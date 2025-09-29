package com.adme.movie_ticket_sale_system.repository;

import com.adme.movie_ticket_sale_system.entity.Showing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ShowingRepository extends JpaRepository<Showing, Integer> {
    List<Showing> findByShowTimeBefore(LocalDateTime time);
}
