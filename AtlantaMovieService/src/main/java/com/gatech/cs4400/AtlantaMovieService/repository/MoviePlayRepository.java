package com.gatech.cs4400.AtlantaMovieService.repository;

import com.gatech.cs4400.AtlantaMovieService.entity.MoviePlay;
import com.gatech.cs4400.AtlantaMovieService.entity.MoviePlayId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoviePlayRepository extends JpaRepository<MoviePlay, MoviePlayId> {
}
