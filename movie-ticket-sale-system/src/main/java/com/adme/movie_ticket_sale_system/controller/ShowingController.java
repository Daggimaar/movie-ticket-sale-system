package com.adme.movie_ticket_sale_system.controller;

import com.adme.movie_ticket_sale_system.entity.Showing;
import com.adme.movie_ticket_sale_system.service.CinemaService;
import com.adme.movie_ticket_sale_system.service.MovieService;
import com.adme.movie_ticket_sale_system.service.ShowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/home")
public class ShowingController {

    private final ShowingService showingService;
    private final MovieService movieService;
    private final CinemaService cinemaService;

    @Autowired
    public ShowingController(ShowingService showingService,
                             MovieService movieService,
                             CinemaService cinemaService) {
        this.showingService = showingService;
        this.movieService = movieService;
        this.cinemaService = cinemaService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String listShowings(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String movieTitle,
            @RequestParam(required = false) Integer cinemaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        List<Showing> showings = showingService.searchShowings(location, genre, movieTitle, cinemaId, date);

        model.addAttribute("showings", showings);
        model.addAttribute("movies", movieService.findAll());
        model.addAttribute("cinemas", cinemaService.findAll());
        model.addAttribute("selectedLocation", location);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("selectedMovieTitle", movieTitle);
        model.addAttribute("selectedCinemaId", cinemaId);
        model.addAttribute("selectedDate", date);

        return "home";
    }
    @RequestMapping(value = "/showing/{id}", method = RequestMethod.GET)
    public String showingDetails(
            @PathVariable Integer id,
            Model model) {

        Showing showing = showingService.findById(id);
        model.addAttribute("showing", showing);

        return "showing-details";
    }

}


