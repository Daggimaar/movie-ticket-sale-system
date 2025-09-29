package com.adme.movie_ticket_sale_system.controller;

import com.adme.movie_ticket_sale_system.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adme.movie_ticket_sale_system.entity.Movie;
import com.adme.movie_ticket_sale_system.entity.Showing;
import com.adme.movie_ticket_sale_system.service.CinemaService;
import com.adme.movie_ticket_sale_system.service.MovieService;
import com.adme.movie_ticket_sale_system.service.ShowingService;
import com.adme.movie_ticket_sale_system.service.TheaterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private final ShowingService showingService;
    private final MovieService movieService;
    private final TheaterService theaterService;
    private final CinemaService cinemaService;
  
    @Autowired
    public AdminController(ShowingService showingService,
                           MovieService movieService,
                           TheaterService theaterService,
                           CinemaService cinemaService) {
        this.showingService = showingService;
        this.movieService = movieService;
        this.theaterService = theaterService;
        this.cinemaService = cinemaService;
    }
  
  
    //adminLogin(GET)
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String adminLoginGET(Model model) {
        model.addAttribute("admin", new User());
        return "adminLogin";
    }

    //adminLogin(POST)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String adminLoginPOST(@ModelAttribute("admin") User user,
                                 HttpSession session,
                                 Model model) {
        if (user.getEmail().equals(adminEmail) && user.getPassword().equals(adminPassword)) {
            session.setAttribute("adminEmail", adminEmail);
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid admin credentials.");
        return "adminLogin";
    }

    //AdminDashboard(GET)
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String adminDashboard(HttpSession session) {
        if (session.getAttribute("adminEmail") == null) {
            return "redirect:/admin/login";
        }
        return "adminAccount";
    }

    //adminLogout(GET)
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
      
      
    @RequestMapping(method = RequestMethod.GET)
    public String adminHome(Model model) {
        model.addAttribute("movies", movieService.findAll());
        model.addAttribute("cinemas", cinemaService.findAll());
        model.addAttribute("theaters", theaterService.findAll());
        model.addAttribute("showings", showingService.findAll());
        model.addAttribute("showing", new Showing());

        return "admin";
    }

    // Movies
    @RequestMapping(value = "/movies/add", method = RequestMethod.GET)
    public String displayAddMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "add-movie";
    }

    @RequestMapping(value = "/movies/add", method = RequestMethod.POST)
    public String submitAddMovieForm(@Valid Movie movie,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add-movie";
        }
        movieService.save(movie);
        redirectAttributes.addFlashAttribute("successMessage", "Movie added successfully!");
        return "redirect:/admin";
    }

    @RequestMapping(value = "/movies/delete/{id}", method = RequestMethod.POST)
    public String deleteMovie(@PathVariable("id") Integer id,
                              RedirectAttributes redirectAttributes) {
        Movie movieToDelete = movieService.findById(id);
        movieService.delete(movieToDelete);
        redirectAttributes.addFlashAttribute("successMessage", "Movie deleted successfully!");
        return "redirect:/admin";
    }


    //Showings
    @RequestMapping(value = "/showings/add", method = RequestMethod.GET)
    public String displayAddShowingForm(Showing showing, Model model) {
        model.addAttribute("showing", showing);
        model.addAttribute("movies", movieService.findAll());
        model.addAttribute("theaters", theaterService.findAll());
        model.addAttribute("cinemas", cinemaService.findAll());
        return "add-showing";
    }

    @RequestMapping(value = "/showings/add", method = RequestMethod.POST)
    public String submitAddShowingForm(Showing showing,
                                RedirectAttributes redirectAttributes) {
        showingService.addShowing(
                showing.getMovie().getId(),
                showing.getTheater().getId(),
                showing.getShowTime()
        );
        redirectAttributes.addFlashAttribute("successMessage", "Showing added successfully!");
        return "redirect:/admin";
    }

    @RequestMapping(value = "/showing/delete/{id}", method = RequestMethod.POST)
    public String deleteShowing(@PathVariable Integer id,
                                RedirectAttributes redirectAttributes) {
        try {
            showingService.deleteShowing(id);
            redirectAttributes.addFlashAttribute("successMessage", "Showing deleted successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin";
    }
}

