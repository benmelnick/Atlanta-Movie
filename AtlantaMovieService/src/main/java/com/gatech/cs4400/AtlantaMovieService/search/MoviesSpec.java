package com.gatech.cs4400.AtlantaMovieService.search;

import com.gatech.cs4400.AtlantaMovieService.entity.Movie;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
        @Spec(path = "movieId.movieName", params = "movieName", spec = LikeIgnoreCase.class),
        @Spec(path = "duration", params = "minDuration", spec = GreaterThanOrEqual.class),
        @Spec(path = "duration", params = "maxDuration", spec = LessThanOrEqual.class),
        @Spec(path = "moviePlayId.movie.movieId.releaseDate", params = "releasedAfter", spec = GreaterThanOrEqual.class),
        @Spec(path = "moviePlayId.movie.movieId.releaseDate", params = "releasedBefore", spec = LessThanOrEqual.class)
})
public interface MoviesSpec extends Specification<Movie> {
}
