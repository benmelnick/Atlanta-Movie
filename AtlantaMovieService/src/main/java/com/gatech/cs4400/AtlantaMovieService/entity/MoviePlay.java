package com.gatech.cs4400.AtlantaMovieService.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table (name = "MOVIEPLAY")
public class MoviePlay {

    @EmbeddedId
    private MoviePlayId moviePlayId;

    @NonNull
    @Temporal (value = TemporalType.DATE)
    private Date playDate;
}
