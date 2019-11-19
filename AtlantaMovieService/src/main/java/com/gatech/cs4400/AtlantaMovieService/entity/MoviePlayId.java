package com.gatech.cs4400.AtlantaMovieService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MoviePlayId implements Serializable {

    @NonNull
    private TheaterId theaterId;

    @NonNull
    private MovieId movieId;
}
