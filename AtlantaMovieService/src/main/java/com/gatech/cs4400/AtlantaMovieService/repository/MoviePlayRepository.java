package com.gatech.cs4400.AtlantaMovieService.repository;

import com.gatech.cs4400.AtlantaMovieService.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;

public interface MoviePlayRepository extends JpaRepository<MoviePlay, MoviePlayId>, JpaSpecificationExecutor<MoviePlay> {

    Boolean existsByMoviePlayIdTheaterTheaterIdTheaterNameAndMoviePlayIdMovieMovieIdMovieName(String theaterName, String movieName);

    MoviePlay findMoviePlayByMoviePlayIdTheaterAndMoviePlayIdMovieMovieIdMovieNameAndMoviePlayIdPlayDate(Theater theater,
                                                                                                         String movieName,
                                                                                                         Date playDate);

}

