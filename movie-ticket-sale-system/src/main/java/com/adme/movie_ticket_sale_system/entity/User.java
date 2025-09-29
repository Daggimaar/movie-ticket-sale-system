package com.adme.movie_ticket_sale_system.entity;

import jakarta.persistence.*;

import java.io.Serial;

@Entity
@Table(name = "registered_users")
public class User implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;


    @Column(name = "profile_picture")
    private byte[] profilePicture;


    public User() {}

    public User(String password, String name, String email) {
        this.password = password;
        this.name = name;
        this.email = email;
    }



    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }
}
