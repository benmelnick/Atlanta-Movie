package com.gatech.cs4400.AtlantaMovieService.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVisitRequest {

    private String username;
    private String theaterName;
    private String companyName;
    private Date visitDate;

}
