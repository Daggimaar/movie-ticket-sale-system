package com.adme.movie_ticket_sale_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Serve uploaded profile pictures
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Maps http://localhost:8080/uploads/... → local folder "uploads/"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
