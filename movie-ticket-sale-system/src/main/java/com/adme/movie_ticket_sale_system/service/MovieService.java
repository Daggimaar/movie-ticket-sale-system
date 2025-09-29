package com.adme.movie_ticket_sale_system.service;

import com.adme.movie_ticket_sale_system.entity.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> findAll();
    Movie findById(int id);

    void save(Movie movie);

    void delete(Movie movie);

}

