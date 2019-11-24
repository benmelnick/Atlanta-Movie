package com.gatech.cs4400.AtlantaMovieService.service;

import com.gatech.cs4400.AtlantaMovieService.entity.Company;
import com.gatech.cs4400.AtlantaMovieService.entity.Theater;
import com.gatech.cs4400.AtlantaMovieService.exception.BadRequestException;
import com.gatech.cs4400.AtlantaMovieService.exception.ResourceNotFoundException;
import com.gatech.cs4400.AtlantaMovieService.payload.CompanyFilterSummary;
import com.gatech.cs4400.AtlantaMovieService.payload.ManagerSummary;
import com.gatech.cs4400.AtlantaMovieService.payload.TheaterSummary;
import com.gatech.cs4400.AtlantaMovieService.repository.CompanyRepository;
import com.gatech.cs4400.AtlantaMovieService.repository.ManagerRepository;
import com.gatech.cs4400.AtlantaMovieService.repository.TheaterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private TheaterService theaterService;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public List<ManagerSummary> getCompanyEmployees(String company) {
        Company retrievedCompany = companyRepository.findById(company)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "name", company));

        return managerRepository.findAllByCompany(retrievedCompany).stream()
                .map(manager -> managerService.entityToSummary(manager)).collect(Collectors.toList());
    }

    public List<TheaterSummary> getCompanyTheaters(String company) {
        return theaterService.getAllTheatersByCompany(company);
    }

    /**
     * gets unassigned managers within a specific company
     * @param company
     * @return
     */
    public List<ManagerSummary> getEligibleManagers(String company) {
        Company retrievedCompany = companyRepository.findById(company)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "name", company));
        return managerRepository.findAll().stream()
                .filter(manager -> managerService.worksForCompany(manager.getUsername(), retrievedCompany)
                        && !managerService.managesTheater(manager.getUsername()))
                .map(manager -> managerService.entityToSummary(manager)).collect(Collectors.toList());
    }

    public Set<CompanyFilterSummary> filterCompanies(Long minTheaters, Long maxTheaters,
                                        Long minEmployees, Long maxEmployees,
                                        Long minCities, Long maxCities) {
        Set<Company> retList = new HashSet<>();

        if (minTheaters != null && maxTheaters != null) {
            retList.addAll(companyRepository.findByNumTheaters(minTheaters, maxTheaters));
        } else if (minTheaters != null) {
            //only min specified
            retList.addAll(companyRepository.findByMinTheaters(minTheaters));
        } else if (maxTheaters != null) {
            //only max specified
            retList.addAll(companyRepository.findByMaxTheaters(maxTheaters));
        }

        if (minCities != null && maxCities != null) {
            List<Company> searchByCity = companyRepository.findByNumCities(minCities, maxCities);
            if (retList.isEmpty()) {
                retList.addAll(searchByCity);
            } else {
                retList.retainAll(searchByCity);
            }
        } else if (minCities != null) {
            //only a minimum is provided
            List<Company> searchByCity = companyRepository.findByMinCities(minCities);
            if (retList.isEmpty()) {
                retList.addAll(searchByCity);
            } else {
                retList.retainAll(searchByCity);
            }
        } else if (maxCities != null) {
            //only a max is provided
            List<Company> searchByCity = companyRepository.findByMaxCities(maxCities);
            if (retList.isEmpty()) {
                retList.addAll(searchByCity);
            } else {
                retList.retainAll(searchByCity);
            }
        }

        if (minEmployees != null && maxEmployees != null) {
            List<Company> searchByEmployee = companyRepository.findByNumEmployees(minEmployees, maxEmployees);
            if (retList.isEmpty()) {
                retList.addAll(searchByEmployee);
            } else {
                retList.retainAll(searchByEmployee);
            }
        } else if (minEmployees != null) {
            //only min specified
            List<Company> searchByEmployee = companyRepository.findByMinEmployees(minEmployees);
            if (retList.isEmpty()) {
                retList.addAll(searchByEmployee);
            } else {
                retList.retainAll(searchByEmployee);
            }
        } else if (maxEmployees != null) {
            //only max specified
            List<Company> searchByEmployee = companyRepository.findByMaxEmployees(maxEmployees);
            if (retList.isEmpty()) {
                retList.addAll(searchByEmployee);
            } else {
                retList.retainAll(searchByEmployee);
            }
        }

        if (minTheaters == null && maxTheaters == null
                && minCities == null && maxCities == null && minEmployees == null && maxEmployees == null) {
            retList.addAll(companyRepository.findAll());
        }

        return retList.stream().map(company -> entityToSummary(company)).collect(Collectors.toSet());
    }

    private CompanyFilterSummary entityToSummary(Company company) {
        int numTheaters = getCompanyTheaters(company.getName()).size();
        int numEmployees = getCompanyTheaters(company.getName()).size();
        int numCities = companyRepository.findNumberOfCitiesCovered(company.getName());

        return new CompanyFilterSummary().builder()
                .company(company.getName())
                .numCitiesCovered(numCities)
                .numEmployees(numEmployees)
                .numTheaters(numTheaters).build();
    }

}
