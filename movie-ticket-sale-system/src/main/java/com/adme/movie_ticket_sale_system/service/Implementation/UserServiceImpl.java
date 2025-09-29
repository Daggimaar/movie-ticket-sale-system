package com.adme.movie_ticket_sale_system.service.Implementation;

import com.adme.movie_ticket_sale_system.entity.User;
import com.adme.movie_ticket_sale_system.repository.UserRepository;
import com.adme.movie_ticket_sale_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getProfilePicture() == null || user.getProfilePicture().length == 0) {
            try {
                InputStream is = getClass().getResourceAsStream("/static/images/default-avatar.png");
                if (is != null) {
                    user.setProfilePicture(is.readAllBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not load default profile picture", e);
            }
        }
        return userRepository.save(user);
    }

    @Override
    public User loginUser(String email, String password) {
        User existing = userRepository.findByEmail(email);
        if (existing == null) {
            throw new IllegalArgumentException("No account found with this email.");
        }
        if (!passwordEncoder.matches(password, existing.getPassword())) {
            throw new IllegalArgumentException("Invalid password.");
        }
        return existing;
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("No account found with that email.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public byte[] uploadProfilePicture(Long id) {
        return userRepository.findById(id)
                .map(User::getProfilePicture)
                .orElse(null);
    }

    @Override
    public User updateProfilePicture(String email, MultipartFile file) {
        if (email == null || file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Invalid request: missing email or file.");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("No account found for this email.");
        }

        try {
            user.setProfilePicture(file.getBytes());
            return userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file.", e);
        }
    }

    @Override
    public User updateAccountInfo(User existingUser, User updatedUser) {
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found.");
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

}


