package com.gatech.cs4400.AtlantaMovieService.controller;

import com.gatech.cs4400.AtlantaMovieService.entity.User;
import com.gatech.cs4400.AtlantaMovieService.payload.*;
import com.gatech.cs4400.AtlantaMovieService.search.UsernameStatusSpec;
import com.gatech.cs4400.AtlantaMovieService.search.VisitHistorySpec;
import com.gatech.cs4400.AtlantaMovieService.security.CurrentUser;
import com.gatech.cs4400.AtlantaMovieService.security.UserPrincipal;
import com.gatech.cs4400.AtlantaMovieService.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        //todo: what the actual fuck is going on???????????????????????
        log.info("------------" + currentUser);
        return userService.getCurrentUser(currentUser);
    }

    @GetMapping("/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userService.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @PostMapping("/approve/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserSummary> approveUser(@PathVariable String username) {
        User user = userService.getByUsername(username);
        UserSummary approvedUser = userService.approveUser(user);

        return new ResponseEntity<>(approvedUser, HttpStatus.OK);
    }

    @PostMapping("/decline/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserSummary> declineUser(@PathVariable String username) {
        User user = userService.getByUsername(username);
        UserSummary approvedUser = userService.declineUser(user);

        return new ResponseEntity<>(approvedUser, HttpStatus.OK);
    }

    @GetMapping("/search/findBy")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserFilterSummary> filterUsers(UsernameStatusSpec spec) {
        return userService.findUsersBy(spec);
    }

    @PostMapping("/visit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> userVisitTheater(@CurrentUser UserPrincipal currentUser,
                                              @Valid @RequestBody UserVisitRequest visitRequest) {

        visitRequest.setUsername(currentUser.getUsername());
        userService.visitTheater(visitRequest);

        return new ResponseEntity<>(new ApiResponse(true, "Theater successfully visited"), HttpStatus.OK);
    }

    @GetMapping("/visit/history")
    @PreAuthorize("hasRole('USER')")
    public List<VisitHistorySummary> filterVisitHistory(@CurrentUser UserPrincipal currentUser, VisitHistorySpec spec) {
        return userService.searchForUserVisits(currentUser.getUsername(), spec);
    }
}
