package com.adme.movie_ticket_sale_system.repository;

import com.adme.movie_ticket_sale_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

