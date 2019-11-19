package com.gatech.cs4400.AtlantaMovieService.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table (name = "ADMIN")
public class Admin {

    @Id
    private String username;
}
