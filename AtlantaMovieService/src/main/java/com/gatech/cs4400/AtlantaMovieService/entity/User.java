package com.gatech.cs4400.AtlantaMovieService.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Builder
@Entity
@Table(name = "USER")
public class User {

    @Id
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    //TODO: modify status to be an enum not a string
    @NonNull
    private String status;

}
