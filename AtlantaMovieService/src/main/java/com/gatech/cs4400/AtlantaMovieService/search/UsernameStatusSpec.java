package com.gatech.cs4400.AtlantaMovieService.search;

import com.gatech.cs4400.AtlantaMovieService.entity.User;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
        @Spec(path = "username", params = "username", spec = LikeIgnoreCase.class),
        @Spec(path = "status", params = "status", spec = Equal.class)
})
public interface UsernameStatusSpec extends Specification<User> {
}
