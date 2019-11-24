package com.gatech.cs4400.AtlantaMovieService.repository;

import com.gatech.cs4400.AtlantaMovieService.entity.Company;
import com.gatech.cs4400.AtlantaMovieService.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, String> {

    Boolean existsByUsername(String username);

    /**
     * returns boolean indicating if there are any records matching the given combination of address attributes
     * this method is called before persisting a new user to ensure that no duplicates are added
     *      which is needed b/c the address is unique for all managers
     * @param streetAddress
     * @param city
     * @param state
     * @param zipcode
     * @return boolean
     */
    Boolean existsByStreetAddressAndCityAndStateAndZipcode(String streetAddress, String city,
                                                           String state, String zipcode);

    List<Manager> findAllByCompany(Company company);

    Boolean existsByUsernameAndCompany(String username, Company company);
}
