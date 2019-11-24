package com.gatech.cs4400.AtlantaMovieService.controller;

import com.gatech.cs4400.AtlantaMovieService.entity.Manager;
import com.gatech.cs4400.AtlantaMovieService.payload.ManagerSummary;
import com.gatech.cs4400.AtlantaMovieService.security.CurrentUser;
import com.gatech.cs4400.AtlantaMovieService.security.UserPrincipal;
import com.gatech.cs4400.AtlantaMovieService.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    /**
     * returns information about the currently logged in user
     * @param currentUser base information about currently logged in user we get using auth token
     * @return summary of logged in manager, including: username, first and last name, address, company, and the
     *         theater they manage (if any)
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('MANAGER')")
    public ManagerSummary getCurrentManager(@CurrentUser UserPrincipal currentUser) {
        Manager currentManager = managerService.getManagerByUsername(currentUser.getUsername());
        return managerService.entityToSummary(currentManager);
    }

}
