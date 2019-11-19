package com.gatech.cs4400.AtlantaMovieService.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table (name = "MOVIE")
public class Movie {

    @EmbeddedId
    private MovieId movieId;

    @NonNull
    private int duration;
}
