package com.gatech.cs4400.AtlantaMovieService.repository;

import com.gatech.cs4400.AtlantaMovieService.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VisitRepository extends JpaRepository<Visit, Integer>, JpaSpecificationExecutor<Visit> {
}
