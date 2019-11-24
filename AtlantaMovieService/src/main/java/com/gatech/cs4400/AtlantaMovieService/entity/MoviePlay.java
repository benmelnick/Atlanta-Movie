package com.gatech.cs4400.AtlantaMovieService.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table (name = "MOVIEPLAY")
public class MoviePlay {

    @EmbeddedId
    private MoviePlayId moviePlayId;

}
