package com.gatech.cs4400.AtlantaMovieService.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class MovieId implements Serializable {

    @NonNull
    private String movieName;
    @NonNull
    @Temporal (value = TemporalType.DATE)
    private Date releaseDate;
}
