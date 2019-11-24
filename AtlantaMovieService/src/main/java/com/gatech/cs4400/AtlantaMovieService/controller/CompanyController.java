package com.gatech.cs4400.AtlantaMovieService.controller;

import com.gatech.cs4400.AtlantaMovieService.payload.CompanyFilterSummary;
import com.gatech.cs4400.AtlantaMovieService.payload.ManagerSummary;
import com.gatech.cs4400.AtlantaMovieService.payload.TheaterSummary;
import com.gatech.cs4400.AtlantaMovieService.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * returns a list of managers for a particular company
     * @param company company whose employees to get, provided by client in the path
     * @return list of summaries of managers, including their: username, first name, last name, street address,
     *         city, state, zipcode, company, and theater
     *         404 if the company the client provides in the path does not exist
     */
    @GetMapping("/{company}/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ManagerSummary> getEmployees(@PathVariable String company) {
        return companyService.getCompanyEmployees(company);
    }

    /**
     * returns a list of theaters for a particular company
     * @param company company whose employees to get, provided by path in client
     * @return list of summaries of theaters, including their: name, company, city, state, manager's username,
     *         and capacity
     *         404 if the company the client provides in the path does not exist
     */
    @GetMapping("/{company}/theaters")
    @PreAuthorize(("hasRole('ADMIN')"))
    public List<TheaterSummary> getTheaters(@PathVariable String company) {
        //todo: returns manager full name instead of just customer?
        return companyService.getCompanyTheaters(company);
    }

    /**
     * returns a list of strings with the names of all companies in the DB
     * this is a public endpoint available w/o authentication, as it is needed in the manager registration page to
     *  populate a dropdown list on the front end
     * @return list of strings of company names in the system
     */
    @GetMapping("/all")
    public List<String> getAllCompanyNames() {
        return companyService.getAllCompanies().stream()
                .map(company -> company.getName())
                .collect(Collectors.toList());
    }

    /**
     * returns a list of managers within a company who are eligible to be assigned to a theater
     * includes the managers within a specific company who have not been assigned to a theater yet
     * @param company company whose employees to get, provided by path in client
     * @return list of summaries of theaters, including their: name, company, city, state, manager's username,
     *         and capacity
     *         404 if the company the client provides in the path does not exist
     */
    @GetMapping("/{company}/eligible")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ManagerSummary> getEligibleManagers(@PathVariable String company) {
        return companyService.getEligibleManagers(company);
    }

    /**
     * allows admin to conduct a search on all companies in the system by the following params: range of number of
     *  theaters, range of number of cities the company has theaters in, the range of the number of employees,
     *  and whether or not the movie has been played already
     * @param minTheaters minimum number of theaters in a company (inclusive)
     * @param maxTheaters maximum number of theaters in a company (inclusive)
     * @param minNumEmployees minimum number of employees in a company (inclusive)
     * @param maxNumEmployees maximum number of employees in a company (inclusive)
     * @param minNumCities minimum number of cities in a company (inclusive)
     * @param maxNumCities maximum number of cities in a company (inclusive)
     * @return list of summaries of companies, including: name, number of cities covered, number of theaters, number
     *         of employees
     */
    @GetMapping("/search/findBy")
    @PreAuthorize("hasRole('ADMIN')")
    public Set<CompanyFilterSummary> findBy(@RequestParam(value = "minTheaters", required = false) Long minTheaters,
                               @RequestParam(value = "maxTheaters", required = false) Long maxTheaters,
                               @RequestParam(value = "minEmployees", required = false) Long minNumEmployees,
                               @RequestParam(value = "maxEmployees", required = false) Long maxNumEmployees,
                               @RequestParam(value = "minCities", required = false) Long minNumCities,
                               @RequestParam(value = "maxCities", required = false) Long maxNumCities) {
        return companyService.filterCompanies(minTheaters, maxTheaters, minNumEmployees, maxNumEmployees,
                minNumCities, maxNumCities);
    }

}
