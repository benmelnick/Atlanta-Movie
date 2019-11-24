package com.gatech.cs4400.AtlantaMovieService.service;

import com.gatech.cs4400.AtlantaMovieService.entity.*;
import com.gatech.cs4400.AtlantaMovieService.exception.BadRequestException;
import com.gatech.cs4400.AtlantaMovieService.exception.ResourceNotFoundException;
import com.gatech.cs4400.AtlantaMovieService.payload.CreateTheaterRequest;
import com.gatech.cs4400.AtlantaMovieService.payload.ManagerFilterMovieSummary;
import com.gatech.cs4400.AtlantaMovieService.payload.TheaterSummary;
import com.gatech.cs4400.AtlantaMovieService.payload.UserFilterTheaterSummary;
import com.gatech.cs4400.AtlantaMovieService.repository.CompanyRepository;
import com.gatech.cs4400.AtlantaMovieService.repository.TheaterRepository;
import com.gatech.cs4400.AtlantaMovieService.search.MoviesPlayingSpec;
import com.gatech.cs4400.AtlantaMovieService.search.MoviesSpec;
import com.gatech.cs4400.AtlantaMovieService.search.TheaterSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private MovieService movieService;

    public Boolean existsByNameAndCompany(String theaterName, String companyName) {
        return theaterRepository.existsByTheaterIdTheaterNameAndTheaterIdCompanyName(theaterName, companyName);
    }

    public Theater getTheaterByNameAndCompany(String theaterName, String companyName) {
        return theaterRepository.findTheaterByTheaterIdTheaterNameAndTheaterIdCompanyName(theaterName, companyName);
    }

    public List<TheaterSummary> getAllTheaters() {
        return theaterRepository.findAll().stream()
                .map(theater -> entityToSummary(theater)).collect(Collectors.toList());
    }

    public List<TheaterSummary> getAllTheatersByCompany(String company) {
        return theaterRepository.findAllByTheaterIdCompanyName(company).stream()
                .map(theater -> entityToSummary(theater))
                .collect(Collectors.toList());
    }

    public Theater createTheater(CreateTheaterRequest request) {
        Company assignedCompany = companyRepository.findById(request.getCompanyName())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "name", request.getCompanyName()));

        Manager assignedManager = managerService.getManagerByUsername(request.getManagerUsername());

        //check that provided manager is not already assigned
        if (managerService.managesTheater(request.getManagerUsername())) {
            throw new BadRequestException("Manager is already assigned to a theater");
        }

        //check that the manager works in the company
        if (!managerService.worksForCompany(request.getManagerUsername(), assignedCompany)) {
            throw new BadRequestException("Manager does not work for this company");
        }

        TheaterId id = new TheaterId().builder()
                .company(assignedCompany)
                .theaterName(request.getTheaterName()).build();

        Theater newTheater = new Theater().builder()
                .theaterId(id)
                .manager(assignedManager)
                .streetAddress(request.getStreetAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipcode(request.getZipcode())
                .capacity(request.getCapacity()).build();

        return theaterRepository.save(newTheater);

    }

    public Set<ManagerFilterMovieSummary> searchForAllPossibleTheaterMovies(Theater theater, MoviesSpec moviesSpec,
                                                                            MoviesPlayingSpec spec) {
        //first get movies playing at the theater
        Set<ManagerFilterMovieSummary> possibleMovies = movieService.searchForMoviesPlaying(spec).stream()
                .filter(moviePlay -> moviePlay.getMoviePlayId().getTheater().equals(theater))
                .map(moviePlay -> moviePlayToFilterSummary(moviePlay))
                .collect(Collectors.toSet());
        //then add movies not playing
        possibleMovies.addAll(searchForAllMoviesNotPlayingAtTheater(theater, moviesSpec));

        return possibleMovies;
    }

    /**
     * returns all movies not playing at the current theater
     * @param moviesSpec
     * @return
     */
    public Set<ManagerFilterMovieSummary> searchForAllMoviesNotPlayingAtTheater(Theater theater, MoviesSpec moviesSpec) {
        return movieService.searchForMovies(moviesSpec).stream()
                .filter(movie -> !movieService.isMoviePlayingAtTheater(theater, movie.getMovieId()))
                .map(movie -> movieToFilterSummary(movie, null))
                .collect(Collectors.toSet());

    }

    public List<UserFilterTheaterSummary> searchForTheaters(TheaterSpec spec) {
        return theaterRepository.findAll(spec).stream()
                .map(theater -> theaterToUserFilterSummary(theater))
                .collect(Collectors.toList());
    }

    private TheaterSummary entityToSummary(Theater theater) {
        return new TheaterSummary().builder()
                .theaterName(theater.getTheaterId().getTheaterName())
                .managerUsername(theater.getManager().getUsername())
                .capacity(theater.getCapacity())
                .city(theater.getCity())
                .state(theater.getState()).build();
    }

    private ManagerFilterMovieSummary movieToFilterSummary(Movie movie, Date playDate) {
        return new ManagerFilterMovieSummary().builder()
                .movieName(movie.getMovieId().getMovieName())
                .releaseDate(movie.getMovieId().getReleaseDate())
                .duration(movie.getDuration())
                .playDate(playDate)
                .build();
    }

    private ManagerFilterMovieSummary moviePlayToFilterSummary(MoviePlay moviePlay) {
        Movie movie = movieService.getByNameAndReleaseDate(moviePlay.getMoviePlayId().getMovie().getMovieId().getMovieName(),
                moviePlay.getMoviePlayId().getMovie().getMovieId().getReleaseDate());
        return movieToFilterSummary(movie, moviePlay.getMoviePlayId().getPlayDate());
    }

    private UserFilterTheaterSummary theaterToUserFilterSummary(Theater theater) {
        String theaterAddress = String.format("%s, %s, %s %s", theater.getStreetAddress(), theater.getCity(),
                theater.getState(), theater.getZipcode());

        return new UserFilterTheaterSummary().builder()
                .address(theaterAddress)
                .companyName(theater.getTheaterId().getCompany().getName())
                .theaterName(theater.getTheaterId().getTheaterName()).build();
    }
}
