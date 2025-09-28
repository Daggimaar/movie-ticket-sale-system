package com.adme.movie_ticket_sale_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import java.time.LocalDate;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Integer id;

    @NotBlank(message = "Title is required")
    @Column(name = "film_name")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description is too long, must be under 500 characters")
    private String description;

    @Pattern(
            regexp = "^https://(www\\.)?youtube\\.com/embed/[\\w\\-]+(\\?.*)?$",
            message = "Trailer URL must be a YouTube embed link"
    )
    private String filmTrailer;

    @NotNull(message = "Release date is required")
    private LocalDate releaseDate;
    private String poster;


    private String genre;

    @NotBlank(message = "Director is required")
    private String director;

    @NotBlank(message = "Language is required")
    private String language;

    // Constructors
    public Movie() {
    }
    public Movie(String name, String description, String filmTrailer,
                 LocalDate releaseDate, String poster, String genre, String director, String language) {
        this.name = name;
        this.description = description;
        this.filmTrailer = filmTrailer;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.genre = genre;
        this.director = director;
        this.language = language;
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFilmTrailer() { return filmTrailer; }
    public void setFilmTrailer(String filmTrailer) { this.filmTrailer = filmTrailer; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}



