package com.gatech.cs4400.AtlantaMovieService.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserSummary {

    private String username;
    private String firstName;
    private String lastName;
    private String status;
    private Boolean isAdmin;
    private Boolean isCustomer;
    private Boolean isManager;

}
