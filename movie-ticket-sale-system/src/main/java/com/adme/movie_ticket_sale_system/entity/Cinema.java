package com.adme.movie_ticket_sale_system.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "cinemas")
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private String name;
    private String location;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    private List<Theater> theaters;

    //Constructors
    public Cinema(){

    }
    public Cinema(String name, String location, List<Theater> theaters) {
        this.name = name;
        this.location = location;
        this.theaters = theaters;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Theater> getTheaters() {
        return theaters;
    }

    public void setTheaters(List<Theater> theaters) {
        this.theaters = theaters;
    }



}
