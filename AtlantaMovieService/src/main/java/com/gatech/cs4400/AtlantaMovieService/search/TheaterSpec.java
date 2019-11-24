package com.gatech.cs4400.AtlantaMovieService.search;

import com.gatech.cs4400.AtlantaMovieService.entity.Theater;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
        @Spec(path = "theaterId.theaterName", params = "theaterName", spec = LikeIgnoreCase.class),
        @Spec(path = "theaterId.company.name", params = "companyName", spec = LikeIgnoreCase.class),
        @Spec(path = "city", params = "city", spec = Equal.class),
        @Spec(path = "state", params = "state", spec = Equal.class)
})
public interface TheaterSpec extends Specification<Theater> {
}
