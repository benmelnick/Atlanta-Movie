package com.gatech.cs4400.AtlantaMovieService.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateTheaterRequest {

    @NotBlank
    private String theaterName;

    @NotBlank
    private String companyName;

    @NotBlank
    private String streetAddress;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String zipcode;

    @NotNull
    private int capacity;

    @NotBlank
    private String managerUsername;
}
