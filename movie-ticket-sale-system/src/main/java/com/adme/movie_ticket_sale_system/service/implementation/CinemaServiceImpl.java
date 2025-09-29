package com.adme.movie_ticket_sale_system.service.implementation;

import com.adme.movie_ticket_sale_system.entity.Cinema;
import com.adme.movie_ticket_sale_system.repository.CinemaRepository;
import com.adme.movie_ticket_sale_system.service.CinemaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaServiceImpl implements CinemaService {
    private final CinemaRepository cinemaRepository;

    public CinemaServiceImpl(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public List<Cinema> findAll() {
        return cinemaRepository.findAll();
    }

    @Override
    public Cinema findById(Integer id) {
        return cinemaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cinema not found with id: " + id));
    }

}
