package com.gatech.cs4400.AtlantaMovieService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table (name = "CUSTOMERVIEWMOVIE")
public class CustomerViewMovie {

    @EmbeddedId
    private CustomerViewMovieId customerViewMovieId;
}
