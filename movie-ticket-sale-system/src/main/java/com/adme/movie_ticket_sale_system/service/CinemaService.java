package com.adme.movie_ticket_sale_system.service;

import com.adme.movie_ticket_sale_system.entity.Cinema;

import java.util.List;

public interface CinemaService {
    List<Cinema> findAll();
    Cinema findById(Integer id);
}
