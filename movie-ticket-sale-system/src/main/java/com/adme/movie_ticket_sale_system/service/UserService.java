package com.adme.movie_ticket_sale_system.service;

import com.adme.movie_ticket_sale_system.entity.User;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {
    User registerUser(User user);

    User loginUser(String email, String password);

    User updateProfilePicture(String email, MultipartFile file);

    byte[] uploadProfilePicture(Long id);

    User findByEmail(String email);

    void save(User user);

    void delete(User user);

    void resetPassword(String email, String newPassword);

    User updateAccountInfo(User existingUser, User updatedUser);
}