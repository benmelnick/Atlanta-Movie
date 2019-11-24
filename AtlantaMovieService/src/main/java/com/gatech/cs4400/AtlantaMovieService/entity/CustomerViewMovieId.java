package com.gatech.cs4400.AtlantaMovieService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class CustomerViewMovieId implements Serializable {

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CreditCardId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CreditCard creditCard;

    @NonNull
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "Company", referencedColumnName = "Company"),
            @JoinColumn(name = "TheaterName", referencedColumnName = "TheaterName"),
            @JoinColumn(name = "MovieName", referencedColumnName = "MovieName"),
            @JoinColumn(name = "PlayDate", referencedColumnName = "PlayDate"),
            @JoinColumn(name = "ReleaseDate", referencedColumnName = "ReleaseDate")
    })
    private MoviePlay moviePlay;
}
