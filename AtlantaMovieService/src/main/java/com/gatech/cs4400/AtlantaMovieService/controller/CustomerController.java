package com.gatech.cs4400.AtlantaMovieService.controller;

import com.gatech.cs4400.AtlantaMovieService.payload.*;
import com.gatech.cs4400.AtlantaMovieService.security.CurrentUser;
import com.gatech.cs4400.AtlantaMovieService.security.UserPrincipal;
import com.gatech.cs4400.AtlantaMovieService.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * adds a new credit card to the current user's account
     * this endpoint is only available to customers, ensuring that only customers can add credit cards
     * @param currentUser information about our current user - determined from the authorization token sent to the endpoint
     * @param request payload user sends, only contains the new credit card number when the controller first receives it
     * @return 201 status if the card was successfully added to the current user's account
     *         400 status code if the card provided is either malformed or currently in the system
     */
    @PostMapping("/addcard")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse> addCreditCard(@CurrentUser UserPrincipal currentUser,
                                           @Valid @RequestBody AddCreditCardRequest request) {
        request.setUsername(currentUser.getUsername());
        customerService.addCreditCard(request);

        return new ResponseEntity<>(new ApiResponse(true, "Credit card successfully added"), HttpStatus.CREATED);
    }

    /**
     * returns a list of credit cards for the currently logged in company
     * @param currentUser information about our current user
     * @return list of summaries of credit cards, including: unique credit card id (UUID), protected credit card number
     */
    @GetMapping("/mycards")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Set<CreditCardSummary> getCustomerCreditCards(@CurrentUser UserPrincipal currentUser) {
        return customerService.getCustomersCards(currentUser.getUsername());
    }

    /**
     * allows a customer to view a currently playing movie
     * @param currentUser information about our current user
     * @param viewMovieRequest information sent from the client about the movie the customer wants to view as well as
     *                         the credit card the customer wishes to use; includes: credit card id, movie name,
     *                         theater name, company name, and date the movie is playing
     * @return 201 status code if the customer was able to successfully view the movie
     *         400 status code if the provided movie is not playing at the provided theater, or the provided credit card
     *          does not belong to the current user
     *         404 if any of the provided parameters do not exist
     */
    @PostMapping("/view")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse> viewMovie(@CurrentUser UserPrincipal currentUser,
                                       @Valid @RequestBody CustomerViewMovieRequest viewMovieRequest) {
        viewMovieRequest.setUsername(currentUser.getUsername());
        customerService.viewMovie(viewMovieRequest);

        return new ResponseEntity<>(new ApiResponse(true, "Movie successfully viewed"), HttpStatus.CREATED);
    }

    /**
     * returns list of movies the current user has seen
     * @param currentUser information about our current user
     * @return list of summaries of the movies customer has seen, including: movie name, theater name, company name,
     *         credit card num used, and date viewed
     */
    @GetMapping("/view/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<ViewHistorySummary> customerViewHistory(@CurrentUser UserPrincipal currentUser) {
        return customerService.getCustomerHistory(currentUser.getUsername());
    }
}
