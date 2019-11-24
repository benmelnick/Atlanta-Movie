package com.gatech.cs4400.AtlantaMovieService.repository;

import com.gatech.cs4400.AtlantaMovieService.entity.Movie;
import com.gatech.cs4400.AtlantaMovieService.entity.MovieId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends JpaRepository<Movie, MovieId>, JpaSpecificationExecutor<Movie> {

    Boolean existsByMovieId(MovieId movieId);

    Boolean existsByMovieIdMovieName(String movieName);

    Movie findByMovieIdMovieName(String movieName);

}
