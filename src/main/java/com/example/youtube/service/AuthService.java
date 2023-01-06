package com.example.youtube.service;


import com.example.youtube.dto.JwtDTO;
import com.example.youtube.dto.profile.ProfileResponseDTO;
import com.example.youtube.dto.auth.LoginDTO;
import com.example.youtube.dto.auth.LoginResponseDTO;
import com.example.youtube.dto.auth.RegistrationDTO;
import com.example.youtube.entity.EmailHistoryEntity;
import com.example.youtube.entity.ProfileEntity;
import com.example.youtube.enums.Language;
import com.example.youtube.enums.ProfileRole;
import com.example.youtube.enums.ProfileStatus;
import com.example.youtube.exp.EmailAlreadyExistsException;
import com.example.youtube.exp.LimitOutPutException;
import com.example.youtube.exp.LoginOrPasswordWrongException;
import com.example.youtube.exp.StatusBlockException;
import com.example.youtube.repository.ProfileRepository;
import com.example.youtube.util.JwtUtil;
import com.example.youtube.util.MD5;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    private final ProfileRepository repository;

    private final MailService mailService;
    private final EmailHistoryService emailHistoryService;

    private final ResourceBundleService resourceBundleService;

    public AuthService(ProfileRepository repository, MailService mailService, EmailHistoryService emailHistoryService, ResourceBundleService resourceBundleService) {
        this.repository = repository;
        this.mailService = mailService;
        this.emailHistoryService = emailHistoryService;
        this.resourceBundleService = resourceBundleService;
    }


    public ProfileResponseDTO registration(RegistrationDTO dto, Language language) {
        Optional<ProfileEntity> exists = repository.findByEmail(dto.getEmail());
        if (exists.isPresent()) {
            ProfileEntity entity = exists.get();
            if (entity.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
                repository.delete(entity);
            } else {
                throw new EmailAlreadyExistsException(resourceBundleService.getMessage("email.exists", language));
            }
        }

        Long countInMinute = emailHistoryService.getCountInMinute(dto.getEmail());
        if (countInMinute > 4) {
            throw new LimitOutPutException(resourceBundleService.getMessage("resent.limit", language));
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5.md5(dto.getPassword()));


        entity.setVisible(true);
        entity.setStatus(ProfileStatus.NOT_ACTIVE);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setRole(ProfileRole.ROLE_USER);

        repository.save(entity);

        Thread thread = new Thread() {
            @Override
            public synchronized void start() {
                String sb = "Salom qalaysan \n" +
                        "Bu test message" +
                        "Click the link : http://localhost:7070/auth/verification/email/" +
                        JwtUtil.encode(entity.getEmail(), ProfileRole.ROLE_USER);
                mailService.sendEmail(dto.getEmail(), "Complete Registration", sb);

                EmailHistoryEntity emailHistoryEntity = new EmailHistoryEntity();
                emailHistoryEntity.setEmail(dto.getEmail());
                emailHistoryEntity.setMessage(sb);
                emailHistoryEntity.setCreatedDate(LocalDateTime.now());

                emailHistoryService.create(emailHistoryEntity);
            }
        };
        thread.start();

        return getDTO(entity);

    }


    public ProfileResponseDTO getDTO(ProfileEntity entity) {

        ProfileResponseDTO profileDTO = new ProfileResponseDTO();
        profileDTO.setId(entity.getId());
        profileDTO.setName(entity.getName());
        profileDTO.setSurname(entity.getSurname());
        profileDTO.setEmail(entity.getEmail());
        profileDTO.setStatus(entity.getStatus());
        profileDTO.setRole(entity.getRole());
        profileDTO.setVisible(entity.getVisible());
        profileDTO.setCreatedDate(entity.getCreatedDate());

        return profileDTO;
    }


    public ProfileEntity getEntity(RegistrationDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPassword(MD5.md5(dto.getPassword()));

        return entity;
    }

    public LoginResponseDTO login(LoginDTO dto, Language language) {
        Optional<ProfileEntity> optional = repository.findByEmailAndPassword(dto.getEmail(), MD5.md5(dto.getPassword()));
        if (optional.isEmpty()) {
            throw new LoginOrPasswordWrongException(resourceBundleService.getMessage("credential.wrong", language.name()));
        }

        ProfileEntity entity = optional.get();
        if (entity.getStatus().equals(ProfileStatus.BLOCK)) {
            throw new StatusBlockException(resourceBundleService.getMessage("profile.status.block",language.name()));
        }

        // TODO
//        if (entity.getStatus().equals(ProfileStatus.NOT_ACTIVE)){
//            throw new ProfileNotActiveException("Profile Is Not Active");
//        }

        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setName(entity.getName());
        responseDTO.setSurname(entity.getSurname());
        responseDTO.setRole(entity.getRole());
        responseDTO.setToken(JwtUtil.encode(entity.getEmail(), entity.getRole()));

        return responseDTO;
    }

    public String verification(String jwt) {


        JwtDTO jwtDTO;
        try {
            jwtDTO = JwtUtil.decodeToken(jwt);
        } catch (JwtException e) {
            return "Verification failed";
        }

        Optional<ProfileEntity> optional = repository.findByEmail(jwtDTO.getUsername());

        if (optional.isEmpty()) {
            return "Verification failed";
        }

        ProfileEntity entity = optional.get();

        if (!entity.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
            return "Verification failed";
        }
        entity.setStatus(ProfileStatus.ACTIVE);

        repository.save(entity);
        return "verification success";
    }
}
