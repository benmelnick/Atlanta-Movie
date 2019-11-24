package com.gatech.cs4400.AtlantaMovieService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table (name = "MANAGER")
public class Manager {

    @Id
    private String username;
    @NonNull
    private String streetAddress;
    @NonNull
    private String city;
    @NonNull
    private String state;
    @NonNull
    private String zipcode;

    @NonNull
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "Company")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Company company;
}
