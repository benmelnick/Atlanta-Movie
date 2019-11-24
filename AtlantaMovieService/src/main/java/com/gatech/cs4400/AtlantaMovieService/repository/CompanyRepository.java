package com.gatech.cs4400.AtlantaMovieService.repository;

import com.gatech.cs4400.AtlantaMovieService.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String>, JpaSpecificationExecutor<Company> {

    Boolean existsByName(String name);

    @Query("select new Company(c.name) from Company c where c.name in " +
            "(select theaterId.company.name from Theater t group by theaterId.company.name " +
            "having count(distinct t.city) >= :minCities)")
    List<Company> findByMinCities(@Param("minCities") Long minCities);

    @Query("select new Company(c.name) from Company c where c.name in " +
            "(select theaterId.company.name from Theater t group by theaterId.company.name " +
            "having count(distinct t.city) <= :maxCities)")
    List<Company> findByMaxCities(@Param("maxCities") Long maxCities);

    @Query("select new Company(c.name) from Company c where c.name in " +
            "(select theaterId.company.name from Theater t group by theaterId.company.name " +
            "having count(distinct t.city) >= :minCities and count(distinct t.city) <= :maxCities) ")
    List<Company> findByNumCities(@Param("minCities") Long minCities, @Param("maxCities") Long maxCities);

    @Query("select new Company(c.name) from Company c where c.name in " +
            "(select company.name from Manager m group by company.name " +
            "having count(distinct m.username) >= :minEmployees) ")
    List<Company> findByMinEmployees(@Param("minEmployees") Long minEmployees);

    @Query("select new Company(c.name) from Company c where c.name in " +
            "(select company.name from Manager m group by company.name " +
            "having count(distinct m.username) <= :maxEmployees) ")
    List<Company> findByMaxEmployees(@Param("maxEmployees") Long maxEmployees);

    @Query("select new Company(c.name) from Company c where c.name in " +
            "(select company.name from Manager m group by company.name " +
            "having count(distinct m.username) >= :minEmployees and count(distinct m.username) <= :maxEmployees) ")
    List<Company> findByNumEmployees(@Param("minEmployees") Long minEmployees, @Param("maxEmployees") Long maxEmployees);

    @Query("select new Company(c.name) from Company c where c.name in " +
            "(select theaterId.company.name from Theater t group by theaterId.company.name " +
            "having count(theaterId.company.name) >= :minTheaters) ")
    List<Company> findByMinTheaters(@Param("minTheaters") Long minTheaters);

    @Query("select new Company(c.name) from Company c where c.name in " +
            "(select theaterId.company.name from Theater t group by theaterId.company.name " +
            "having count(theaterId.company.name) <= :maxTheaters) ")
    List<Company> findByMaxTheaters(@Param("maxTheaters") Long maxTheaters);

    @Query("select new Company(c.name) from Company c where c.name in " +
            "(select theaterId.company.name from Theater t group by theaterId.company.name " +
            "having count(theaterId.company.name) >= :minTheaters and count(theaterId.company.name) <= :maxTheaters) ")
    List<Company> findByNumTheaters(@Param("minTheaters") Long minTheaters, @Param("maxTheaters") Long maxTheaters);

    @Query("select count(company) from Theater t where t.theaterId.company.name = :company")
    Integer findNumberOfCitiesCovered(@Param("company") String company);
}
