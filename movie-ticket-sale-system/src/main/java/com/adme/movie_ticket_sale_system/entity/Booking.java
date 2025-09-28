package com.adme.movie_ticket_sale_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name="booking")
public class Booking {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String seatNumber;

    @ManyToOne
    @JoinColumn(name = "showing_id")
    private Showing showing;

    //constructors
    public Booking(){}

    public Booking(String email, String seatNumber, Showing showing) {
        this.email = email;
        this.seatNumber = seatNumber;
        this.showing = showing;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Showing getShowing() {
        return showing;
    }

    public void setShowing(Showing showing) {
        this.showing = showing;
    }

}

