package com.gatech.cs4400.AtlantaMovieService.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerViewMovieRequest {

    private String username;

    @NotBlank
    private String creditCardId;

    @NotBlank
    private String movieName;

    @NotBlank
    private String theaterName;

    @NotBlank
    private String companyName;

    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date playDate;
}
