package com.gatech.cs4400.AtlantaMovieService.security;

import com.gatech.cs4400.AtlantaMovieService.entity.User;
import com.gatech.cs4400.AtlantaMovieService.exception.DeclinedUserException;
import com.gatech.cs4400.AtlantaMovieService.repository.UserRepository;
import com.sun.deploy.net.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findById(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username : " + username)
                );

        if (user.getStatus().equals("Declined")) {
            throw new DeclinedUserException("User is declined with username: " + username);
        }

        return UserPrincipal.create(user);
    }
}
