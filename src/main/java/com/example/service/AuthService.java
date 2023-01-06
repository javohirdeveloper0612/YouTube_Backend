package com.example.service;

import com.example.dto.*;
import com.example.entity.EmailHistoryEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.EmailAlreadyExistsException;
import com.example.exp.LimitOutPutException;
import com.example.exp.LoginOrPasswordWrongException;
import com.example.exp.StatusBlockException;
import com.example.repository.ProfileRepository;
import com.example.util.JwtUtil;
import com.example.util.MD5;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    private final ProfileRepository repository;

    private final EmailHistoryService emailHistoryService;
    private final ResourceBundleService resourceBundleService;

    private final MailService mailService;

    public AuthService(ProfileRepository repository, EmailHistoryService emailHistoryService, ResourceBundleService resourceBundleService, MailService mailService) {
        this.repository = repository;
        this.emailHistoryService = emailHistoryService;
        this.resourceBundleService = resourceBundleService;
        this.mailService = mailService;
    }

    public ProfileResponseDTO registration(UserRegistrationDTO dto, Language language) {

        Optional<ProfileEntity> optional = repository.findByEmail(dto.getEmail());
        if (optional.isPresent()){
            ProfileEntity entity = optional.get();
            if (entity.getStatus().equals(ProfileStatus.NOT_ACTIVE)){
                repository.delete(entity);
            }else {
                throw new EmailAlreadyExistsException(resourceBundleService.getMessage("email.exists",language));
            }
        }
        Long countMinute= emailHistoryService.getCountInMinute(dto.getEmail());

        if (countMinute > 4){
            throw new LimitOutPutException(resourceBundleService.getMessage("resent.limit",language));
        }

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName(dto.getName());
        profileEntity.setSurname(dto.getSurname());
        profileEntity.setEmail(dto.getEmail());
        profileEntity.setPassword(MD5.md5(dto.getPassword()));

        profileEntity.setVisible(true);
        profileEntity.setStatus(ProfileStatus.NOT_ACTIVE);
        profileEntity.setCreatedDate(LocalDateTime.now());
        profileEntity.setRole(ProfileRole.ROLE_USER);

        repository.save(profileEntity);


        Thread thread = new Thread() {
            @Override
            public synchronized  void start(){
                String str = "Assalomualeykum  \n" +
                        "tasdiqlash link \n" +
                        "Click the link http://localhost:7070/auth/verification/email/" +
                        JwtUtil.encode(profileEntity.getEmail(),ProfileRole.ROLE_USER);
                        mailService.sendEmail(dto.getEmail(),"Complete Registration",str);

                EmailHistoryEntity emailHistory = new EmailHistoryEntity();
                emailHistory.setEmail(dto.getEmail());
                emailHistory.setMessage(str);
                emailHistory.setCreatedDate(LocalDateTime.now());

                emailHistoryService.create(emailHistory);
            }
        };

        thread.start();


        return getDTO(profileEntity);
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


    public LoginResponseDTO login(LoginDTO dto, Language language) {
        Optional<ProfileEntity> optional = repository.findByEmailAndPassword(dto.getEmail(),MD5.md5(dto.getPassword()));

        if (optional.isEmpty()){
            throw new LoginOrPasswordWrongException(resourceBundleService.getMessage("creadential.wrong",language));
        }

        ProfileEntity entity = optional.get();
        if (entity.getStatus().equals(ProfileStatus.BLOCK)){
            throw new StatusBlockException(resourceBundleService.getMessage("profile.status.block",language));
        }
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setName(entity.getName());
        responseDTO.setSurname(entity.getSurname());
        responseDTO.setRole(entity.getRole());
        responseDTO.setToken(JwtUtil.encode(entity.getEmail(),entity.getRole()));

        return responseDTO;
    }
}
