package com.gatech.cs4400.AtlantaMovieService.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSummary {

    //todo: add view history and status
    private String username;
    private Set<String> creditCards;
}
