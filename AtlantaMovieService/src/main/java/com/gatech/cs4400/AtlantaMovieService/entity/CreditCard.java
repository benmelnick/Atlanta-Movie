package com.gatech.cs4400.AtlantaMovieService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table (name = "CREDITCARD")
public class CreditCard {

    @Id
    private String creditCardNum;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "Username")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Customer username;
}
