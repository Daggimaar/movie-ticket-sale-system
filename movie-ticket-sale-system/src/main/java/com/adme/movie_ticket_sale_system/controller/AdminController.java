package com.adme.movie_ticket_sale_system.controller;

import com.adme.movie_ticket_sale_system.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

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
    }
}

