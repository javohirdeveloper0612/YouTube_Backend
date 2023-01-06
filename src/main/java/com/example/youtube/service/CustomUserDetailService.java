package com.example.youtube.service;

import com.example.youtube.config.security.CustomUserDetails;
import com.example.youtube.entity.ProfileEntity;
import com.example.youtube.repository.ProfileRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // alish
        Optional<ProfileEntity> optional = profileRepository.findByEmail(username);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("Bad Cretensional");
        }

        ProfileEntity profile = optional.get();
//        return new CustomUserDetails(profile);
        return new CustomUserDetails(profile.getId(),profile.getEmail(), profile.getPassword(), profile.getRole());
    }
}
