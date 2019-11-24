package com.gatech.cs4400.AtlantaMovieService.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserSignUpRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
