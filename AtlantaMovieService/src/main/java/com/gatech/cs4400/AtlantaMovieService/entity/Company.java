package com.gatech.cs4400.AtlantaMovieService.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table (name = "COMPANY")
public class Company {

    @Id
    private String name;
}
