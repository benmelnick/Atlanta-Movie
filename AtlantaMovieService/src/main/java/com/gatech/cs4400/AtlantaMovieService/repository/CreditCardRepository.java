package com.gatech.cs4400.AtlantaMovieService.repository;

import com.gatech.cs4400.AtlantaMovieService.entity.CreditCard;
import com.gatech.cs4400.AtlantaMovieService.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {

    Boolean existsByCreditCardNum(String creditCardNum);

    Set<CreditCard> findAllByCustomer(Customer customer);

    Boolean existsByCreditCardIdAndCustomerUsername(String creditCardId, String customerUsername);
}
