package com.adme.movie_ticket_sale_system.controller;

import com.adme.movie_ticket_sale_system.entity.Showing;
import com.adme.movie_ticket_sale_system.service.ShowingService;
import com.adme.movie_ticket_sale_system.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/showing")
public class BookingController {
    private final ShowingService showingService;
    private final BookingService bookingService;

    @Autowired
    public BookingController(ShowingService showingService, BookingService bookingService) {
        this.showingService = showingService;
        this.bookingService = bookingService;
    }

    // Start purchase (show seat map and booked seats)
    @RequestMapping(value = "/purchase/{id}", method = RequestMethod.GET)
    public String startBooking(@PathVariable Integer id, Model model) {
        Showing showing = showingService.findById(id);

        Map<String, List<String>> seatMap = showingService.generateSeatMap(id);
        Set<String> booking = bookingService.getBooking(id);

        model.addAttribute("showing", showing);
        model.addAttribute("seatMap", seatMap);
        model.addAttribute("booking", booking);

        return "purchase";
    }

    // Confirm seat purchase
    @RequestMapping(value = "/purchase/{id}", method = RequestMethod.POST)
    public String processBooking(@PathVariable Integer id,
                                 @RequestParam String selectedSeats,
                                 @RequestParam String email,
                                 RedirectAttributes redirectAttributes) {
        try {
            bookingService.bookSeatsWithValidation(id, selectedSeats, email);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/showing/purchase/" + id;
        }

        redirectAttributes.addFlashAttribute("selectedSeats", selectedSeats);
        redirectAttributes.addFlashAttribute("email", email);

        return "redirect:/showing/purchase/" + id + "/confirmation";
    }

    // Confirmation page
    @RequestMapping(value = "/purchase/{id}/confirmation", method = RequestMethod.GET)
    public String bookingConfirmation(@PathVariable Integer id,
                                      @ModelAttribute("selectedSeats") String selectedSeats,
                                      @ModelAttribute("email") String email,
                                      Model model) {
        List<String> seatLabels = Arrays.asList(selectedSeats.split(","));
        model.addAttribute("selectedSeats", seatLabels);

        Showing showing = showingService.findById(id);
        model.addAttribute("showing", showing);
        model.addAttribute("email", email);

        return "confirmation";
    }
}
