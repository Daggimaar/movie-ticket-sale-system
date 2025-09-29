package com.adme.movie_ticket_sale_system.service;
import com.adme.movie_ticket_sale_system.entity.Showing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public interface ShowingService {
    List<Showing> findAll();
    void addShowing(Integer movieId, Integer theaterId, LocalDateTime showTime);

    void deleteShowing(Integer showingId);
    Showing findById(Integer id);

    List<Showing> searchShowings(String location,
                                 String genre,
                                 String movieTitle,
                                 Integer cinemaId,
                                 LocalDate date);

    Map<String, List<String>> generateSeatMap(Integer showingId);

    void deleteExpiredShowings();


}
