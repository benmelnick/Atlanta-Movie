package com.gatech.cs4400.AtlantaMovieService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class MoviePlayId implements Serializable {

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "TheaterName", referencedColumnName = "TheaterName"),
            @JoinColumn(name = "Company", referencedColumnName = "Company")
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Theater theater;

    @NonNull
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumns(value = {
            @JoinColumn (name = "MovieName", referencedColumnName = "MovieName"),
            @JoinColumn (name = "ReleaseDate", referencedColumnName = "ReleaseDate")
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Movie movie;

    @NonNull
    @Temporal (value = TemporalType.DATE)
    private Date playDate;
}
