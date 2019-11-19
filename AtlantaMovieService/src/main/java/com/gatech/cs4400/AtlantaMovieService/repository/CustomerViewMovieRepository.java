package com.gatech.cs4400.AtlantaMovieService.repository;

import com.gatech.cs4400.AtlantaMovieService.entity.CustomerViewMovie;
import com.gatech.cs4400.AtlantaMovieService.entity.CustomerViewMovieId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerViewMovieRepository extends JpaRepository<CustomerViewMovie, CustomerViewMovieId> {
}
