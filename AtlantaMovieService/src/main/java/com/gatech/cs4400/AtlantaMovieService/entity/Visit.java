package com.gatech.cs4400.AtlantaMovieService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table (name = "VISIT")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int visitId;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "User")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "TheaterName", referencedColumnName = "TheaterName"),
            @JoinColumn(name = "Company", referencedColumnName = "Company")
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Theater theater;

    @NonNull
    @Temporal (value = TemporalType.DATE)
    private Date visitDate;

}
