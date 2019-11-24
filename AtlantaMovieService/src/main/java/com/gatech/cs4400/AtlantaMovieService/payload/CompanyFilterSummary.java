package com.gatech.cs4400.AtlantaMovieService.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyFilterSummary {

    private String company;
    private int numCitiesCovered;
    private int numTheaters;
    private int numEmployees;


}
