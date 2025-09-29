package com.adme.movie_ticket_sale_system.controller;

import com.adme.movie_ticket_sale_system.entity.User;
import com.adme.movie_ticket_sale_system.repository.UserRepository;
import com.adme.movie_ticket_sale_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@Controller
@RequestMapping("/account")
public class AccountModController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder; // inject PasswordEncoder
    private final UserRepository userRepository;

    @Autowired
    public AccountModController(UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    // Dashboard(GET)
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboardGET(HttpSession session, Model model) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) return "redirect:/login";

        User user = userService.findByEmail(email);
        if (user == null) {
            session.invalidate();
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("deleteRequest", new User());
        return "account";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.POST)
    public String dashboardPOST() {
        return "redirect:/account/dashboard"; // or handle post logic
    }



    //ProfilePicture(GET)

    @GetMapping("/picture/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> profilePicture(@PathVariable Long id) {
        byte[] image = userService.uploadProfilePicture(id);

        if (image == null) {
            try {
                InputStream in = getClass().getResourceAsStream("/static/default-avatar.png");
                if (in != null) {
                    image = in.readAllBytes();
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (IOException e) {
                return ResponseEntity.notFound().build();
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/upload-picture", method = RequestMethod.POST)
    public String profilePicturePOST(@RequestParam("file") MultipartFile file,
                                     HttpSession session,
                                     Model model) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) return "redirect:/login";

        try {
            User user = userService.updateProfilePicture(email, file);
            model.addAttribute("message", "Profile picture updated!");
            model.addAttribute("user", user);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", userService.findByEmail(email));
        }

        model.addAttribute("deleteRequest", new User());
        return "account";
    }






    // EditAccount(GET/POST)
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editAccountGET(HttpSession session, Model model) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) return "redirect:/login";

        User user = userService.findByEmail(email);
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        return "account";
    }

    // EditAccount (POST)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editAccountPOST(@ModelAttribute("user") User updatedUser,
                                  HttpSession session,
                                  Model model) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) return "redirect:/login";

        User existingUser = userService.findByEmail(email);
        if (existingUser == null) {
            session.invalidate();
            return "redirect:/login";
        }

        try {
            User savedUser = userService.updateAccountInfo(existingUser, updatedUser);
            session.setAttribute("userEmail", savedUser.getEmail());
            model.addAttribute("message", "Account updated successfully!");
            model.addAttribute("user", savedUser);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", existingUser);
        }

        model.addAttribute("deleteRequest", new User());
        return "account";
    }


    // DeleteAccount(GET)
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteAccountGET(Model model, HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) {
            return "redirect:/login";
        }
        model.addAttribute("deleteRequest", new User());
        return "account";
    }

    // DeleteAccount (POST)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteAccountPOST(@ModelAttribute("deleteRequest") User request,
                                HttpSession session,
                                Model model) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) return "redirect:/login";

        User user = userService.findByEmail(email);
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            model.addAttribute("error", "Invalid password. Please try again.");
            model.addAttribute("user", user);
            model.addAttribute("deleteRequest", new User());
            return "account"; // stay on account page
        }

        userRepository.delete(user);
        session.invalidate();
        return "redirect:/login?deleted";
    }
}
