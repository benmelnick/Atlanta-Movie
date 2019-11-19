package com.gatech.cs4400.AtlantaMovieService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MovieId implements Serializable {

    @NonNull
    private String movieName;
    @NonNull
    @Temporal (value = TemporalType.DATE)
    private Date releaseDate;
}
