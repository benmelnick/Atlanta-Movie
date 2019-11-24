package com.gatech.cs4400.AtlantaMovieService.repository;

import com.gatech.cs4400.AtlantaMovieService.entity.CreditCard;
import com.gatech.cs4400.AtlantaMovieService.entity.CustomerViewMovie;
import com.gatech.cs4400.AtlantaMovieService.entity.CustomerViewMovieId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomerViewMovieRepository extends JpaRepository<CustomerViewMovie, CustomerViewMovieId> {

    List<CustomerViewMovie> findAllByCustomerViewMovieIdCreditCard(CreditCard card);

    List<CustomerViewMovie> findAllByCustomerViewMovieIdMoviePlayMoviePlayIdPlayDateAndCustomerViewMovieIdCreditCardCustomerUsername(Date playDate, String username);

}
