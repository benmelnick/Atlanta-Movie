package com.gatech.cs4400.AtlantaMovieService.controller;

import com.gatech.cs4400.AtlantaMovieService.entity.Employee;
import com.gatech.cs4400.AtlantaMovieService.entity.User;
import com.gatech.cs4400.AtlantaMovieService.payload.*;
import com.gatech.cs4400.AtlantaMovieService.repository.EmployeeRepository;
import com.gatech.cs4400.AtlantaMovieService.security.JwtTokenProvider;
import com.gatech.cs4400.AtlantaMovieService.service.CustomerService;
import com.gatech.cs4400.AtlantaMovieService.service.ManagerService;
import com.gatech.cs4400.AtlantaMovieService.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /**
     * signs a user into our system, returning a token that allows the user to reach our endpoints
     * the token must be sent along to any subsequent endpoints in order to access them
     * @param loginRequest input from the client containing username and password
     * @return 200 status code along with authorization token
     *         401 status code if the login attempt fails
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    /**
     * creates a new user in the DB's user table
     * @param signUpRequest input from the client containing username, password, first name, and last name
     * @return 201 created status code if the client sends valid data and the new user was successfully created
     *         400 status code if data is invalid (username already exists)
     */
    @PostMapping("/signup/user")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody UserSignUpRequest signUpRequest) {
        userService.validateSignUpRequest(signUpRequest);
        //register the new customer
        User result = userService.registerUser(signUpRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true,
                "User registered successfully"));

    }

    /**
     * creates a new user in the DB's customers and user tables
     * @param signUpRequest input from the client containing all user info (username, password, first name, last name)
     *                      as well as a list of between 1 and 5 credit card numbers
     * @return 201 status code if the client sends valid data and the new user was successfully created
     *         400 status code if any of the arguments are invalid
     */
    @PostMapping("/signup/customer")
    public ResponseEntity<ApiResponse> registerCustomer(@Valid @RequestBody CustomerSignUpRequest signUpRequest) {
        customerService.validateSignUpRequest(signUpRequest);
        //register the new user
        User result = userService.registerUser(signUpRequest);

        //register the new customer
        customerService.registerCustomer(signUpRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    /**
     * creates a new user in the DB's user and manager tables
     * @param managerSignUpRequest input from the client containing all user info along with company name and address
     * @return 201 status code if the client sends valid data and the new user was successfully created
     *         400 status code if any of the arguments are invalid (i.e. formatted improperly or a field is already registered)
     *         404 status code if the company provided does not exist
     */
    @PostMapping("/signup/manager")
    public ResponseEntity<ApiResponse> registerManager(@Valid @RequestBody ManagerSignUpRequest managerSignUpRequest) {
        managerService.validateSignUpRequest(managerSignUpRequest);
        //register the new user
        User result = userService.registerUser(managerSignUpRequest);

        //register the new employee
        //todo: just get rid of employee entirely in the DB - it is useless
        employeeRepository.save(new Employee().builder().
                username(managerSignUpRequest.getUsername()).build());

        //register the new manager
        managerService.registerManager(managerSignUpRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    /**
     * creates a new user in the DB's user, manager, and customer tables
     * @param request input from the client containing all user info along with company name, address, and credit cards
     * @return 201 status code if the client sends valid data and the new user was successfully created
     *         400 status code if any of the arguments are invalid (i.e. formatted improperly or a field is already registered)
     *         404 status code if the company provided does not exist
     */
    @PostMapping("/signup/manager-customer")
    public ResponseEntity<ApiResponse> registerManagerCustomer(@Valid @RequestBody ManagerCustomerSignupRequest request) {
        customerService.validateManagerSignUpRequest(request);
        //register the new user
        User result = userService.registerUser(request);

        //register the new employee
        employeeRepository.save(new Employee().builder().
                username(request.getUsername()).build());

        //register the new manager
        managerService.registerManager(request);

        customerService.registerCustomerManager(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
