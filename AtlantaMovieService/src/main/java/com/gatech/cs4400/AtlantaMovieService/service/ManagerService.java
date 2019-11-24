package com.gatech.cs4400.AtlantaMovieService.service;

import com.gatech.cs4400.AtlantaMovieService.entity.Company;
import com.gatech.cs4400.AtlantaMovieService.entity.Manager;
import com.gatech.cs4400.AtlantaMovieService.entity.Theater;
import com.gatech.cs4400.AtlantaMovieService.entity.User;
import com.gatech.cs4400.AtlantaMovieService.exception.BadRequestException;
import com.gatech.cs4400.AtlantaMovieService.exception.ResourceNotFoundException;
import com.gatech.cs4400.AtlantaMovieService.payload.ManagerCustomerSignupRequest;
import com.gatech.cs4400.AtlantaMovieService.payload.ManagerSignUpRequest;
import com.gatech.cs4400.AtlantaMovieService.payload.ManagerSummary;
import com.gatech.cs4400.AtlantaMovieService.repository.CompanyRepository;
import com.gatech.cs4400.AtlantaMovieService.repository.ManagerRepository;
import com.gatech.cs4400.AtlantaMovieService.repository.TheaterRepository;
import com.gatech.cs4400.AtlantaMovieService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private UserRepository userRepository;

    public Manager getManagerByUsername(String username) {
        return managerRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Manager", "username", username));
    }

    public Boolean existsByUsername(String username) {
        return managerRepository.existsByUsername(username);
    }

    public Boolean worksForCompany(String username, Company company) {
        return managerRepository.existsByUsernameAndCompany(username, company);
    }

    public Boolean validateSignUpRequest(ManagerSignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Username is already registered");
        }
        if (!companyExists(signUpRequest.getCompanyName())) {
            throw new ResourceNotFoundException("Company", "name", signUpRequest.getCompanyName());
        }

        if (addressExists(signUpRequest.getStreetAddress(), signUpRequest.getCity(),
                signUpRequest.getState(), signUpRequest.getZipcode())) {
            throw new BadRequestException("Address is already registered");
        }
        return true;
    }

    public void registerManager(ManagerSignUpRequest request) {
        //persist the manager
        Manager manager = new Manager().builder()
                .username(request.getUsername())
                .streetAddress(request.getStreetAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipcode(request.getZipcode())
                .company(companyRepository.findById(request.getCompanyName()).get())
                .build();
        managerRepository.save(manager);
    }

    public Boolean companyExists(String company) {
        return companyRepository.existsByName(company);
    }

    public Boolean addressExists(String streetAddress, String city, String state, String zip) {
        return managerRepository.existsByStreetAddressAndCityAndStateAndZipcode(streetAddress, city, state, zip);
    }

    public Boolean managesTheater(String username) {
        Manager manager = managerRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Manager", "customer", username));
        return theaterRepository.existsByManager(manager);
    }

    public Theater getManagerTheater(String username) {
        if (!managesTheater(username)) {
            //will also throw a resource not found if the manager doesnt exist
            throw new BadRequestException("You are not currently assigned to a theater");
        }
        Manager manager = getManagerByUsername(username);
        return theaterRepository.findByManager(manager);
    }

    public ManagerSummary entityToSummary(Manager manager) {
        User user = userRepository.findById(manager.getUsername()).get();
        String theater = "";
        if (managesTheater(manager.getUsername())) {
            theater = getManagerTheater(manager.getUsername()).getTheaterId().getTheaterName();
        }
        return new ManagerSummary().builder()
                .username(manager.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .streetAddress(manager.getStreetAddress())
                .city(manager.getCity())
                .state(manager.getState())
                .zipcode(manager.getZipcode())
                .company(manager.getCompany().getName())
                .theater(theater).build();
    }
}
