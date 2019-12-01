package com.gatech.cs4400.AtlantaMovieService.search;

import com.gatech.cs4400.AtlantaMovieService.entity.MoviePlay;
import net.kaczmarzyk.spring.data.jpa.domain.*;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
        @Spec(path = "moviePlayId.theater.theaterId.company.name", params = "companyName", spec = LikeIgnoreCase.class),
        @Spec(path = "moviePlayId.movie.movieId.movieName", params = "movieName", spec = LikeIgnoreCase.class),
        @Spec(path = "moviePlayId.movie.duration", params = "minDuration", spec = GreaterThanOrEqual.class),
        @Spec(path = "moviePlayId.movie.duration", params = "maxDuration", spec = LessThanOrEqual.class),
        @Spec(path = "moviePlayId.movie.movieId.releaseDate", params = "releasedAfter", spec = GreaterThanOrEqual.class),
        @Spec(path = "moviePlayId.movie.movieId.releaseDate", params = "releasedBefore", spec = LessThanOrEqual.class),
        @Spec(path = "moviePlayId.playDate", params = "playedAfter", spec = GreaterThanOrEqual.class),
        @Spec(path = "moviePlayId.playDate", params = "playedBefore", spec = LessThanOrEqual.class),
        @Spec(path = "moviePlayId.theater.city", params = "city", spec = Equal.class),
        @Spec(path = "moviePlayId.theater.state", params = "state", spec = Equal.class)
})
public interface MoviesPlayingSpec extends Specification<MoviePlay> {
}
