package com.adme.movie_ticket_sale_system.service;

import com.adme.movie_ticket_sale_system.entity.Theater;

import java.util.List;

public interface TheaterService {
    Theater findById(Integer id);
    List<Theater> findAll();
}
