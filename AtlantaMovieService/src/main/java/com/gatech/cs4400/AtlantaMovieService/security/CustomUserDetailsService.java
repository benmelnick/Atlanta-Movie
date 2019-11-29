package com.gatech.cs4400.AtlantaMovieService.security;

import com.gatech.cs4400.AtlantaMovieService.entity.User;
import com.gatech.cs4400.AtlantaMovieService.exception.DeclinedUserException;
import com.gatech.cs4400.AtlantaMovieService.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

        if (!user.getStatus().equals("Approved")) {
            throw new DeclinedUserException("User is not approved with username: " + username);
        }

        return UserPrincipal.create(user);
    }
}
