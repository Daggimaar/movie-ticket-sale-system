package com.adme.movie_ticket_sale_system.repository;

import com.adme.movie_ticket_sale_system.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CinemaRepository extends JpaRepository<Cinema, Integer> {

}
