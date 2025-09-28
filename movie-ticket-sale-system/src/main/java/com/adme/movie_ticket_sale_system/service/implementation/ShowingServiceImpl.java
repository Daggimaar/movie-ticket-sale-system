package com.adme.movie_ticket_sale_system.service.implementation;

import com.adme.movie_ticket_sale_system.entity.Movie;
import com.adme.movie_ticket_sale_system.entity.Showing;
import com.adme.movie_ticket_sale_system.entity.Theater;
import com.adme.movie_ticket_sale_system.repository.*;
import com.adme.movie_ticket_sale_system.service.ShowingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class ShowingServiceImpl implements ShowingService {
    private final ShowingRepository showingRepository;
    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    @Autowired
    public ShowingServiceImpl(ShowingRepository showingRepository, BookingRepository bookingRepository, MovieRepository movieRepository, TheaterRepository theaterRepository) {
        this.showingRepository = showingRepository;
        this.bookingRepository = bookingRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
    }

    @Override
    public void addShowing(Integer movieId, Integer theaterId, LocalDateTime showTime) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id " + movieId));
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new IllegalArgumentException("Theater not found with id " + theaterId));

        Showing showing = new Showing();
        showing.setMovie(movie);
        showing.setTheater(theater);
        showing.setShowTime(showTime);
        showing.setAvailableSeats(theater.getTotalSeats());
        showingRepository.save(showing);
    }

    @Override
    public void deleteShowing(Integer id) {
        Showing showing = showingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Showing not found with id: " + id));

        showingRepository.delete(showing);
    }

    @Override
    public List<Showing> findAll() {
        return showingRepository.findAll(Sort.by("showTime").ascending());
    }

    @Override
    public List<Showing> searchShowings(String location, String genre, String movieTitle,
                                        Integer cinemaId, LocalDate date) {
        List<Showing> showings = showingRepository.findAll();

        return showings.stream()
                .filter(s -> location == null || location.isBlank() ||
                        (s.getTheater().getCinema().getLocation() != null &&
                                s.getTheater().getCinema().getLocation().toLowerCase()
                                        .contains(location.toLowerCase().trim())))
                .filter(s -> genre == null || genre.isBlank() ||
                        (s.getMovie().getGenre() != null &&
                                s.getMovie().getGenre().toLowerCase()
                                        .contains(genre.toLowerCase().trim())))
                .filter(s -> movieTitle == null || movieTitle.isBlank() ||
                        (s.getMovie().getName() != null &&
                                s.getMovie().getName().toLowerCase()
                                        .contains(movieTitle.toLowerCase().trim())))
                .filter(s -> cinemaId == null || s.getTheater().getCinema().getId().equals(cinemaId))
                .filter(s -> date == null || s.getShowTime().toLocalDate().equals(date))
                .sorted(Comparator.comparing(Showing::getShowTime))
                .toList();
    }

    @Override
    public Showing findById(Integer id) {
        return showingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Showing not found"));
    }

    @Override
    public Map<String, List<String>> generateSeatMap(Integer showingId) {
        Showing showing = findById(showingId);
        int seatsPerRow = 25;
        int totalSeats = showing.getTheater().getTotalSeats();
        int totalRows = (int) Math.ceil((double) totalSeats / seatsPerRow);

        List<String> rowLabels = new ArrayList<>();
        for (int i = 0; i < totalRows; i++) {
            rowLabels.add(String.valueOf((char) ('A' + i)));
        }

        Map<String, List<String>> seatMap = new LinkedHashMap<>();
        for (int rowIndex = 0; rowIndex < totalRows; rowIndex++) {
            String rowLabel = rowLabels.get(rowIndex);
            List<String> seatsInRow = new ArrayList<>();
            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                int seatIndex = rowIndex * seatsPerRow + seatNum;
                if (seatIndex <= totalSeats) {
                    seatsInRow.add(rowLabel + seatNum);
                }
            }
            seatMap.put(rowLabel, seatsInRow);
        }

        return seatMap;
    }

    @Override
    @Scheduled(fixedRate = 3600000) // every hour
    @Transactional
    public void deleteExpiredShowings() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(12); //12 hours after
        List<Showing> expiredShowings = showingRepository.findByShowTimeBefore(cutoffTime);

        for (Showing s : expiredShowings) {

            // removing bookings first
            bookingRepository.deleteByShowing(s);
            showingRepository.delete(s);
        }
    }


}

