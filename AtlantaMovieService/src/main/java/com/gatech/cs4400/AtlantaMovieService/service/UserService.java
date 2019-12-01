package com.gatech.cs4400.AtlantaMovieService.service;

import com.gatech.cs4400.AtlantaMovieService.entity.*;
import com.gatech.cs4400.AtlantaMovieService.exception.AppException;
import com.gatech.cs4400.AtlantaMovieService.exception.BadRequestException;
import com.gatech.cs4400.AtlantaMovieService.exception.ResourceNotFoundException;
import com.gatech.cs4400.AtlantaMovieService.payload.*;
import com.gatech.cs4400.AtlantaMovieService.repository.*;
import com.gatech.cs4400.AtlantaMovieService.search.UsernameStatusSpec;
import com.gatech.cs4400.AtlantaMovieService.search.VisitHistorySpec;
import com.gatech.cs4400.AtlantaMovieService.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private TheaterService theaterService;

    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        //todo: instantiate these values based on the roles
        Boolean isAdmin = isUserAdmin(currentUser.getUsername());
        Boolean isCustomer = isUserCustomer(currentUser.getUsername());
        Boolean isManager = isUserManager(currentUser.getUsername());

        return new UserSummary().builder()
                .username(currentUser.getUsername())
                .firstName(currentUser.getFirstName())
                .lastName(currentUser.getLastName())
                .status(currentUser.getStatus())
                .isAdmin(isAdmin)
                .isCustomer(isCustomer)
                .isManager(isManager).build();
    }

    public User getByUsername(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "customer", username));
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Boolean validateSignUpRequest(UserSignUpRequest signUpRequest) {
        if (existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Username is already registered");
        }
        return true;
    }

    public User registerUser(UserSignUpRequest signUpRequest) {
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        roles.add(userRole);

        if (signUpRequest instanceof CustomerSignUpRequest || signUpRequest instanceof ManagerCustomerSignupRequest) {
            //this user being added is also a customer, add that role
            Role customerRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                    .orElseThrow(() -> new AppException("Customer Role not set."));
            roles.add(customerRole);
        }

        if (signUpRequest instanceof ManagerSignUpRequest){
            //this user being added is also a manager, add that role
            Role managerRole = roleRepository.findByName(RoleName.ROLE_MANAGER)
                    .orElseThrow(() -> new AppException("Manager Role not set."));
            roles.add(managerRole);
        }


        User user = new User().builder()
                .username(signUpRequest.getUsername())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .status("Pending")
                .roles(roles)
                .build();

        return userRepository.save(user);
    }

    public List<UserFilterSummary> findUsersBy(UsernameStatusSpec spec) {
        return userRepository.findAll(spec).stream().map(user -> entityToFilterSummary(user)).collect(Collectors.toList());
    }

    public UserSummary approveUser(User user) {
        user.setStatus("Approved");
        return entityToSummary(userRepository.save(user));
    }

    public UserSummary declineUser(User user) {
        if (user.getStatus().equals("Approved")) {
            throw new BadRequestException("Approved users cannot be declined.");
        }
        user.setStatus("Declined");
        return entityToSummary(userRepository.save(user));
    }

    public void visitTheater(UserVisitRequest visitRequest) {
        //check that the theater exists
        if (!theaterService.existsByNameAndCompany(visitRequest.getTheaterName(), visitRequest.getCompanyName())) {
            throw new ResourceNotFoundException("Theater", "name and company",
                    visitRequest.getTheaterName() + ", " + visitRequest.getCompanyName());
        }

        Theater visitedTheater = theaterService.getTheaterByNameAndCompany(visitRequest.getTheaterName(),
                visitRequest.getCompanyName());

        //create a new visit and persist
        Visit newVisit = new Visit().builder()
                .visitDate(visitRequest.getVisitDate())
                .user(userRepository.findById(visitRequest.getUsername()).get())
                .theater(visitedTheater).build();

        visitRepository.save(newVisit);
    }

    public List<VisitHistorySummary> searchForUserVisits(String username, VisitHistorySpec spec) {
        return visitRepository.findAll(spec).stream()
                .filter(visit -> visit.getUser().getUsername().equals(username))
                .map(visit -> visitToHistorySummary(visit))
                .collect(Collectors.toList());
    }

    public Boolean isUserCustomer(String username) {
        return customerService.existsByUsername(username);
    }

    public Boolean isUserManager(String username) {
        return managerService.existsByUsername(username);
    }

    public Boolean isUserAdmin(String username) {
        return adminRepository.existsByUsername(username);
    }

    /**
     * all users coming in to this method have all been verified as existed
     * called from the search method that finds all of the users through the repo
     * @param user
     * @return
     */
    private UserFilterSummary entityToFilterSummary(User user) {
        //calculate number of credit cards and determine user role

        String userType = "User";
        if (isUserCustomer(user.getUsername()) && isUserManager(user.getUsername())) {
            userType = "Manager-Customer";
        } else if (isUserCustomer(user.getUsername())) {
            userType = "Customer";
        } else if (isUserManager(user.getUsername())) {
            userType = "Manager";
        }
        int numCards = 0;
        Optional<Customer> customerLookUp = customerService.getCustomerByUsername(user.getUsername());
        if (customerLookUp.isPresent()) {
            numCards = creditCardRepository.findAllByCustomer(customerLookUp.get()).size();
        }

        return new UserFilterSummary().builder()
                .username(user.getUsername())
                .creditCardCount(numCards)
                .status(user.getStatus())
                .userType(userType)
                .build();
    }

    private UserSummary entityToSummary(User user) {
        return new UserSummary().builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .status(user.getStatus())
                .isAdmin(isUserAdmin(user.getUsername()))
                .isManager(isUserManager(user.getUsername()))
                .isCustomer(isUserCustomer(user.getUsername()))
                .build();
    }

    private VisitHistorySummary visitToHistorySummary(Visit visit) {
        String theaterAddress = String.format("%s, %s, %s %s", visit.getTheater().getStreetAddress(),
                visit.getTheater().getCity(), visit.getTheater().getState(), visit.getTheater().getZipcode());

        return new VisitHistorySummary().builder()
                .address(theaterAddress)
                .companyName(visit.getTheater().getTheaterId().getCompany().getName())
                .theaterName(visit.getTheater().getTheaterId().getTheaterName())
                .visitDate(visit.getVisitDate()).build();
    }
}
