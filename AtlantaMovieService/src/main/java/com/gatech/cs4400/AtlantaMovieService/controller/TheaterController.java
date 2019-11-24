package com.gatech.cs4400.AtlantaMovieService.controller;

import com.gatech.cs4400.AtlantaMovieService.entity.Theater;
import com.gatech.cs4400.AtlantaMovieService.exception.BadRequestException;
import com.gatech.cs4400.AtlantaMovieService.payload.*;
import com.gatech.cs4400.AtlantaMovieService.search.MoviesPlayingSpec;
import com.gatech.cs4400.AtlantaMovieService.search.MoviesSpec;
import com.gatech.cs4400.AtlantaMovieService.search.TheaterSpec;
import com.gatech.cs4400.AtlantaMovieService.security.CurrentUser;
import com.gatech.cs4400.AtlantaMovieService.security.UserPrincipal;
import com.gatech.cs4400.AtlantaMovieService.service.ManagerService;
import com.gatech.cs4400.AtlantaMovieService.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/theater")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @Autowired
    private ManagerService managerService;

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createTheater(@Valid @RequestBody CreateTheaterRequest theaterRequest) {
        Theater result = theaterService.createTheater(theaterRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/theater/{theaterName}")
                .buildAndExpand(result.getTheaterId().getTheaterName()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Theater registered successfully"));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public List<TheaterSummary> getAll() {
        //todo: make this just return strings???????
        return theaterService.getAllTheaters();
    }

    @GetMapping("/mytheater/search")
    @PreAuthorize("hasRole('MANAGER')")
    public Set<ManagerFilterMovieSummary> searchForMoviesInTheater(@RequestParam(value = "notPlayed") Boolean notPlayed,
                                                        MoviesSpec moviesSpec,
                                                        MoviesPlayingSpec moviesPlayingSpec,
                                                        @CurrentUser UserPrincipal userPrincipal) {
        if (!managerService.managesTheater(userPrincipal.getUsername())) {
            //manager does not manage a theater
            throw new BadRequestException("You are not currently assigned to a theater");
        }

        Theater currTheater = managerService.getManagerTheater(userPrincipal.getUsername());
        if (notPlayed) {
            //only show movies not playing at theater
            return theaterService.searchForAllMoviesNotPlayingAtTheater(currTheater, moviesSpec);
        }
        return theaterService.searchForAllPossibleTheaterMovies(currTheater, moviesSpec, moviesPlayingSpec);
    }

    @GetMapping("/search/findBy")
    @PreAuthorize("hasRole('USER')")
    public List<UserFilterTheaterSummary> searchForTheaters(TheaterSpec spec) {
        return theaterService.searchForTheaters(spec);
    }

}
