package com.adme.movie_ticket_sale_system.controller;

import com.adme.movie_ticket_sale_system.entity.User;
import com.adme.movie_ticket_sale_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Register
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerGET(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPOST(@ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            userService.registerUser(user);
            model.addAttribute("message", "Registration successful for " + user.getName());
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // Login
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGET(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginPOST(@ModelAttribute("user") User user,
                            BindingResult result,
                            HttpSession session,
                            Model model) {
        if (result.hasErrors()) {
            return "login";
        }
        try {
            User existing = userService.logInUser(user.getEmail(), user.getPassword());
            session.setAttribute("userEmail", existing.getEmail());
            return "redirect:/account/dashboard";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }



    // Reset password
    @RequestMapping(value = "/reset-password", method = RequestMethod.GET)
    public String resetPasswordGET(Model model) {
        model.addAttribute("user", new User());
        return "reset_password";
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public String resetPassword(String email, String newPassword, Model model) {
        try {
            userService.resetPassword(email, newPassword);
            model.addAttribute("resetMessage", "Password reset successful. Please log in with your new password.");
            return "reset_password";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "reset_password";
        }
    }

    // Logout
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
