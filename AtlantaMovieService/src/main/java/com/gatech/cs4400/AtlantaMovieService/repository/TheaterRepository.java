package com.gatech.cs4400.AtlantaMovieService.repository;

import com.gatech.cs4400.AtlantaMovieService.entity.Manager;
import com.gatech.cs4400.AtlantaMovieService.entity.Theater;
import com.gatech.cs4400.AtlantaMovieService.entity.TheaterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, TheaterId>, JpaSpecificationExecutor<Theater> {

    Boolean existsByManager(Manager manager);

    Theater findByManager(Manager manager);

    List<Theater> findAllByTheaterIdCompanyName(String companyName);

    Boolean existsByTheaterIdTheaterNameAndTheaterIdCompanyName(String theaterName, String companyName);

    Theater findTheaterByTheaterIdTheaterNameAndTheaterIdCompanyName(String theaterName, String companyName);
}
