package com.gatech.cs4400.AtlantaMovieService.controller;

import com.gatech.cs4400.AtlantaMovieService.entity.Movie;
import com.gatech.cs4400.AtlantaMovieService.entity.MoviePlay;
import com.gatech.cs4400.AtlantaMovieService.entity.Theater;
import com.gatech.cs4400.AtlantaMovieService.payload.*;
import com.gatech.cs4400.AtlantaMovieService.search.MoviesPlayingSpec;
import com.gatech.cs4400.AtlantaMovieService.security.CurrentUser;
import com.gatech.cs4400.AtlantaMovieService.security.UserPrincipal;
import com.gatech.cs4400.AtlantaMovieService.service.ManagerService;
import com.gatech.cs4400.AtlantaMovieService.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('MANAGER')")
    public List<String> getAllMovieNames() {
        return movieService.getAllMovies().stream()
                .map(movie -> movie.getMovieId().getMovieName())
                .collect(Collectors.toList());
    }

    @GetMapping("/all/playing")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Set<String> getAllPlayingMovieNames() {
        return movieService.getAllMoviesPlaying().stream()
                .map(moviePlay -> moviePlay.getMoviePlayId().getMovie().getMovieId().getMovieName())
                .collect(Collectors.toSet());
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createMovie(@Valid @RequestBody CreateMovieRequest movieRequest) {
        Movie result = movieService.createMovie(movieRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("movie/{movieName}")
                .buildAndExpand(result.getMovieId().getMovieName()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Movie registered successfully"));
    }

    @PostMapping("/schedule")
    @PreAuthorize(("hasRole('MANAGER')"))
    public ResponseEntity<ApiResponse> scheduleMovie(@Valid @RequestBody ScheduleMovieRequest movieRequest,
                                           @CurrentUser UserPrincipal currentUser) {
        //currentUser is guaranteed to be a manager since only managers can access this endpoint

        Theater theater = managerService.getManagerTheater(currentUser.getUsername());

        MoviePlay result = movieService.scheduleMovie(movieRequest, theater);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/movie/{movieNameAndDate}")
                .buildAndExpand(result.getMoviePlayId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Movie scheduled successfully"));
    }

    @GetMapping("/playing/search")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<MoviePlaySummary> getAllMoviesPlaying(MoviesPlayingSpec moviesPlayingSpec) {
        return movieService.searchForMoviesPlaying(moviesPlayingSpec).stream()
                .map(moviePlay -> movieService.entityToSummary(moviePlay))
                .collect(Collectors.toList());
    }
}
