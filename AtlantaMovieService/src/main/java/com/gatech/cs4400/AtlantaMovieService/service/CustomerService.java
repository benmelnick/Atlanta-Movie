package com.gatech.cs4400.AtlantaMovieService.service;

import com.gatech.cs4400.AtlantaMovieService.entity.*;
import com.gatech.cs4400.AtlantaMovieService.exception.BadRequestException;
import com.gatech.cs4400.AtlantaMovieService.exception.ResourceNotFoundException;
import com.gatech.cs4400.AtlantaMovieService.payload.*;
import com.gatech.cs4400.AtlantaMovieService.repository.CreditCardRepository;
import com.gatech.cs4400.AtlantaMovieService.repository.CustomerRepository;
import com.gatech.cs4400.AtlantaMovieService.repository.CustomerViewMovieRepository;
import com.gatech.cs4400.AtlantaMovieService.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CustomerViewMovieRepository customerViewMovieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private TheaterService theaterService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private UserRepository userRepository;

    public Boolean existsByUsername(String username) {
        return customerRepository.existsByUsername(username);
    }

    public Optional<Customer> getCustomerByUsername(String username) {
        return customerRepository.findById(username);
    }

    public Boolean validateSignUpRequest(CustomerSignUpRequest signUpRequest) {
        //validate the user sign up
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Username is already registered");
        }

        //validate the customer signup

        //validate number of credit cards
        if (signUpRequest.getCreditCards() == null || signUpRequest.getCreditCards().size() < 1 || signUpRequest.getCreditCards().size() > 5) {
            throw new BadRequestException("Customer must have at least 1 credit card and at most 5 credit cards");
        }
        //validate that provided credit cards are unique
        for (String creditCardNum : signUpRequest.getCreditCards()) {
            validateCreditCard(creditCardNum);
        }
        return true;
    }

    public Boolean validateManagerSignUpRequest(ManagerCustomerSignupRequest signUpRequest) {
        CustomerSignUpRequest customerSignUpRequest = new CustomerSignUpRequest();
        customerSignUpRequest.setUsername(signUpRequest.getUsername());
        customerSignUpRequest.setCreditCards(signUpRequest.getCreditCards());

        return managerService.validateSignUpRequest(signUpRequest) && validateSignUpRequest(customerSignUpRequest);
    }

    public void registerCustomer(CustomerSignUpRequest signUpRequest) {
        //persist the customer
        Customer customer = new Customer().builder().username(signUpRequest.getUsername()).build();
        customerRepository.save(customer);

        //persist the customer's credit cards
        for (String creditCardNum : signUpRequest.getCreditCards()) {
            //todo: maybe create an overloaded method addCreditCard that takes in just a string?
            AddCreditCardRequest request = new AddCreditCardRequest().builder()
                    .creditCardNum(creditCardNum)
                    .username(signUpRequest.getUsername()).build();
            addCreditCard(request);
        }
    }

    public void registerCustomerManager(ManagerCustomerSignupRequest request) {
        //create a customer sign request - we only need the username and cards
        CustomerSignUpRequest customerSignUpRequest = new CustomerSignUpRequest();
        customerSignUpRequest.setUsername(request.getUsername());
        customerSignUpRequest.setCreditCards(request.getCreditCards());

        registerCustomer(customerSignUpRequest);
    }

    public void addCreditCard(AddCreditCardRequest addCreditCardRequest) {
        validateCreditCard(addCreditCardRequest.getCreditCardNum());
        //validate that user does not already have 5 cards
        if (findCardsByUsername(addCreditCardRequest.getUsername()).size() == 5) {
            throw new BadRequestException("Customer cannot have more than 5 credit cards");
        }
        //validate inputs
        if (addCreditCardRequest.getCreditCardNum().length() != 16) {
            throw new BadRequestException("Credit cards must contain exactly 16 digits");
        }
        if (creditCardExists(addCreditCardRequest.getCreditCardNum())) {
            throw new BadRequestException("Credit card already in use");
        }

        CreditCard newCard = new CreditCard().builder()
                .customer(customerRepository.findById(addCreditCardRequest.getUsername()).get())
                .creditCardNum(addCreditCardRequest.getCreditCardNum()).build();
        creditCardRepository.save(newCard);
    }

    public Boolean creditCardExists(String creditCardNum) {
        return creditCardRepository.existsByCreditCardNum(creditCardNum);
    }

    public Set<CreditCard> findCardsByUsername(String username) {
        Customer customer = customerRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customer", username));
        return creditCardRepository.findAllByCustomer(customer);
    }

    public Set<CreditCardSummary> getCustomersCards(String username) {
        return findCardsByUsername(username).stream()
                .map(card -> cardToSummary(card))
                .collect(Collectors.toSet());
    }

    public void viewMovie(CustomerViewMovieRequest viewMovieRequest) {
        //validate that movie exists
        if (!movieService.existsByName(viewMovieRequest.getMovieName())) {
            throw new ResourceNotFoundException("Movie", "name", viewMovieRequest.getMovieName());
        }
        //validate that theater exists
        if (!theaterService.existsByNameAndCompany(viewMovieRequest.getTheaterName(), viewMovieRequest.getCompanyName())) {
            throw new ResourceNotFoundException("Theater", "name and company",
                    viewMovieRequest.getTheaterName() + ", " + viewMovieRequest.getCompanyName());
        }

        //validate that user has not already watched 3 movies on the given day
        if (customerViewMovieRepository.findAllByCustomerViewMovieIdMoviePlayMoviePlayIdPlayDateAndCustomerViewMovieIdCreditCardCustomerUsername(viewMovieRequest.getPlayDate(), viewMovieRequest.getUsername()).size() >= 3) {
            throw new BadRequestException("Cannot watch more than 3 movies on the same day");
        }

        //get the movie that we know exists
        Movie movie = movieService.getMovieByName(viewMovieRequest.getMovieName());

        //get the theater that we know exists
        Theater theater = theaterService.getTheaterByNameAndCompany(viewMovieRequest.getTheaterName(),
                viewMovieRequest.getCompanyName());

        //validate that movie is playing at theater
        if (!movieService.isMoviePlayingAtTheater(theater, movie.getMovieId())) {
            throw new BadRequestException("The requested movie is not currently playing at the provided theater");
        }

        //validate that the credit card actually belongs to you
        if (!creditCardRepository.existsByCreditCardIdAndCustomerUsername(viewMovieRequest.getCreditCardId(),
                viewMovieRequest.getUsername())) {
            throw new BadRequestException("This credit card does not belong to you!");
        }

        //the movie is playing at the theater - find the movie play
        MoviePlay moviePlay = movieService.getMoviePlayByMovieNameTheaterAndPlayDate(viewMovieRequest.getMovieName(),
                theater, viewMovieRequest.getPlayDate());

        //create a new view and persist it
        CustomerViewMovie newView = new CustomerViewMovie().builder()
                .customerViewMovieId(new CustomerViewMovieId().builder()
                    .moviePlay(moviePlay)
                    .creditCard(creditCardRepository.findById(viewMovieRequest.getCreditCardId()).get())
                    .build())
                .build();

        customerViewMovieRepository.save(newView);
    }

    public List<ViewHistorySummary> getCustomerHistory(String username) {
        //start by getting all of the customer's credit cards since that info is stored in the view table
        Set<CreditCard> userCards = findCardsByUsername(username);

        List<ViewHistorySummary> retList = new ArrayList<>();

        //find movies that the customer viewed with those cards
        for (CreditCard card : userCards) {
            retList.addAll(customerViewMovieRepository.findAllByCustomerViewMovieIdCreditCard(card).stream()
                .map(customerViewMovie -> entityToSummary(customerViewMovie)).collect(Collectors.toList()));
        }
        return retList;
    }

    private ViewHistorySummary entityToSummary(CustomerViewMovie customerViewMovie) {
        String movieName = customerViewMovie.getCustomerViewMovieId().getMoviePlay().getMoviePlayId().getMovie().getMovieId().getMovieName();
        String companyName = customerViewMovie.getCustomerViewMovieId().getMoviePlay().getMoviePlayId().getTheater().getTheaterId().getCompany().getName();
        String theaterName = customerViewMovie.getCustomerViewMovieId().getMoviePlay().getMoviePlayId().getTheater().getTheaterId().getTheaterName();
        String creditCardNum = protectedCreditCardString(customerViewMovie.getCustomerViewMovieId().getCreditCard().getCreditCardNum());
        Date viewDate = customerViewMovie.getCustomerViewMovieId().getMoviePlay().getMoviePlayId().getPlayDate();

        return new ViewHistorySummary().builder()
                .movieName(movieName)
                .companyName(companyName)
                .theaterName(theaterName)
                .creditCardNum(creditCardNum)
                .viewDate(viewDate).build();
    }

    private CreditCardSummary cardToSummary(CreditCard card) {
        return new CreditCardSummary().builder()
                .creditCardId(card.getCreditCardId())
                .creditCardNum(protectedCreditCardString(card.getCreditCardNum()))
                .build();
    }

    private String protectedCreditCardString(String creditCardNum) {
        return "************" + creditCardNum.substring(12);
    }

    /**
     * validates a given credit card
     * throws exceptions if there is a problem
     * does nothing otherwise
     * @param creditCardNum
     */
    private void validateCreditCard(String creditCardNum) {
        try {
            Long num = Long.parseLong(creditCardNum);
        } catch (NumberFormatException | NullPointerException e) {
            throw new BadRequestException("Credit cards can only contain numeric values");
        }
        if (creditCardNum.length() != 16) {
            throw new BadRequestException("Credit cards must contain exactly 16 digits, rejecting: " + creditCardNum);
        }
        if (creditCardExists(creditCardNum)) {
            throw new BadRequestException("Credit card already in use, rejecting: " + creditCardNum);
        }
    }
}
