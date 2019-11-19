package com.gatech.cs4400.AtlantaMovieService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CustomerViewMovieId implements Serializable {

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CreditCardNum")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CreditCard creditCard;

    @NonNull
    private MoviePlayId moviePlayId;
}
