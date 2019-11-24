package com.gatech.cs4400.AtlantaMovieService.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data

public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
