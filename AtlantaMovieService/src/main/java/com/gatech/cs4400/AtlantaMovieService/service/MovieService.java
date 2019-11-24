package com.gatech.cs4400.AtlantaMovieService.service;

import com.gatech.cs4400.AtlantaMovieService.entity.*;
import com.gatech.cs4400.AtlantaMovieService.exception.BadRequestException;
import com.gatech.cs4400.AtlantaMovieService.exception.ResourceNotFoundException;
import com.gatech.cs4400.AtlantaMovieService.payload.CreateMovieRequest;
import com.gatech.cs4400.AtlantaMovieService.payload.MoviePlaySummary;
import com.gatech.cs4400.AtlantaMovieService.payload.ScheduleMovieRequest;
import com.gatech.cs4400.AtlantaMovieService.repository.MoviePlayRepository;
import com.gatech.cs4400.AtlantaMovieService.repository.MovieRepository;
import com.gatech.cs4400.AtlantaMovieService.repository.TheaterRepository;
import com.gatech.cs4400.AtlantaMovieService.search.MoviesPlayingSpec;
import com.gatech.cs4400.AtlantaMovieService.search.MoviesSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MoviePlayRepository moviePlayRepository;

    public List<MoviePlay> getAllMoviesPlaying() {
        return moviePlayRepository.findAll();
    }

    public List<MoviePlay> searchForMoviesPlaying(MoviesPlayingSpec spec) {
        return moviePlayRepository.findAll(spec);
    }

    public Boolean existsByName(String movieName) {
        return movieRepository.existsByMovieIdMovieName(movieName);
    }

    public Movie getMovieByName(String movieName) {
        return movieRepository.findByMovieIdMovieName(movieName);
    }

    public MoviePlay getMoviePlayByMovieNameTheaterAndPlayDate(String movie, Theater theater, Date playDate) {
        return moviePlayRepository.findMoviePlayByMoviePlayIdTheaterAndMoviePlayIdMovieMovieIdMovieNameAndMoviePlayIdPlayDate(theater, movie, playDate);
    }

    public Boolean existsByNameAndReleaseDate(String movieName, Date releaseDate) {
        MovieId id = new MovieId().builder()
                .movieName(movieName)
                .releaseDate(releaseDate).build();

        return movieRepository.existsByMovieId(id);
    }

    public Boolean isMoviePlayingAtTheater(Theater theater, MovieId movieId) {
        return moviePlayRepository.existsByMoviePlayIdTheaterTheaterIdTheaterNameAndMoviePlayIdMovieMovieIdMovieName(theater.getTheaterId().getTheaterName(),
                movieId.getMovieName());
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> searchForMovies(MoviesSpec spec) {
        return movieRepository.findAll(spec);
    }

    public Movie getByNameAndReleaseDate(String movieName, Date releaseDate) {
        MovieId id = new MovieId().builder()
                .movieName(movieName)
                .releaseDate(releaseDate).build();

        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "movieId", id));
    }

    public Movie createMovie(CreateMovieRequest movieRequest) {
        if (existsByNameAndReleaseDate(movieRequest.getMovieName(), movieRequest.getReleaseDate())) {
            throw new BadRequestException("This movie currently exists for the provided release date");
        }
        MovieId id = new MovieId().builder()
                .movieName(movieRequest.getMovieName())
                .releaseDate(movieRequest.getReleaseDate()).build();

        Movie newMovie = new Movie().builder()
                .movieId(id)
                .duration(movieRequest.getDuration()).build();

        return movieRepository.save(newMovie);
    }

    public MoviePlay scheduleMovie(ScheduleMovieRequest movieRequest, Theater theater) {
        //validate movie exists
        if (!existsByNameAndReleaseDate(movieRequest.getMovieName(), movieRequest.getReleaseDate())) {
            throw new ResourceNotFoundException("Movie", "name and release date",
                    movieRequest.getMovieName() + ", " + movieRequest.getPlayDate());
        }
        //validate play date is after release date
        if (movieRequest.getPlayDate().before(movieRequest.getReleaseDate())) {
            throw new BadRequestException("Movie play must be scheduled after release date");
        }
        Movie retrievedMovie = getByNameAndReleaseDate(movieRequest.getMovieName(), movieRequest.getReleaseDate());
        MoviePlayId id = new MoviePlayId().builder()
                .movie(retrievedMovie)
                .theater(theater)
                .playDate(movieRequest.getPlayDate()).build();

        MoviePlay newMoviePlay = new MoviePlay().builder()
                .moviePlayId(id)
                .build();

        return moviePlayRepository.save(newMoviePlay);
    }

    public MoviePlaySummary entityToSummary(MoviePlay moviePlay) {
        Theater theater = moviePlay.getMoviePlayId().getTheater();
        String address = theater.getStreetAddress() + ", " + theater.getCity() + ", " + theater.getState() + " " + theater.getZipcode();

        return new MoviePlaySummary().builder()
                .movieName(moviePlay.getMoviePlayId().getMovie().getMovieId().getMovieName())
                .theaterName(moviePlay.getMoviePlayId().getTheater().getTheaterId().getTheaterName())
                .companyName(moviePlay.getMoviePlayId().getTheater().getTheaterId().getCompany().getName())
                .address(address)
                .playDate(moviePlay.getMoviePlayId().getPlayDate()).build();
    }
}
