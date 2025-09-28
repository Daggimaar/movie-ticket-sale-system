package com.adme.movie_ticket_sale_system.service.implementation;
import com.adme.movie_ticket_sale_system.entity.Movie;
import com.adme.movie_ticket_sale_system.repository.MovieRepository;
import com.adme.movie_ticket_sale_system.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public Movie findById(int id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));
    }

    @Override
    public void save(Movie movie) { movieRepository.save(movie);}

    @Override
    public void delete(Movie movie) {movieRepository.delete(movie);
    }
}