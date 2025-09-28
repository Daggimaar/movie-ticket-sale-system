package com.adme.movie_ticket_sale_system.service.implementation;

import com.adme.movie_ticket_sale_system.entity.Theater;
import com.adme.movie_ticket_sale_system.repository.TheaterRepository;
import com.adme.movie_ticket_sale_system.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterServiceImpl implements TheaterService {
    private final TheaterRepository theaterRepository;

    @Autowired
    public TheaterServiceImpl(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    @Override
    public Theater findById(Integer id) {
        return theaterRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Theater not found with id: " + id)
        );
    }

    @Override
    public List<Theater> findAll() {
        return theaterRepository.findAll();
    }

}
