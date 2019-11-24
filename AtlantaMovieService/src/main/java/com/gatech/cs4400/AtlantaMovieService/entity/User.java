package com.gatech.cs4400.AtlantaMovieService.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USER")
public class User implements Serializable {

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USERROLES",
            joinColumns = @JoinColumn(name = "Username"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

}
