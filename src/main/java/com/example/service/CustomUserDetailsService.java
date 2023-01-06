package com.example.service;


import com.example.config.security.CustomUserDetail;
import com.example.entity.ProfileEntity;
import com.example.repository.ProfileRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ProfileRepository repository;

    public CustomUserDetailsService(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<ProfileEntity> profile = repository.findByEmail(login);

        if (profile.isEmpty()) {
            throw new UsernameNotFoundException("Bad Credentials");
        }

        return new CustomUserDetail(profile.get());

    }
}
