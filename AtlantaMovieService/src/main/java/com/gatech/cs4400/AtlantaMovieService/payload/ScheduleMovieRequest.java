package com.gatech.cs4400.AtlantaMovieService.payload;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ScheduleMovieRequest {

    @NotBlank
    private String movieName;

    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date releaseDate;

    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date playDate;
}
