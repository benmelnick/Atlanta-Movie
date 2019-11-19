package com.gatech.cs4400.AtlantaMovieService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table (name = "THEATER")
public class Theater {

    @EmbeddedId
    private TheaterId theaterId;

    @NonNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "Manager")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Manager manager;

    @NonNull
    private String streetAddress;

    @NonNull
    private String city;

    @NonNull
    private String state;

    @NonNull
    private String zipcode;

    @NonNull
    private int capacity;
}
