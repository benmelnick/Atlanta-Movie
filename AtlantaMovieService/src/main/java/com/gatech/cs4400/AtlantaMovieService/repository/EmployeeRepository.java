package com.gatech.cs4400.AtlantaMovieService.repository;

import com.gatech.cs4400.AtlantaMovieService.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
}
