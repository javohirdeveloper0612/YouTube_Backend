package com.example.service;

import com.example.dto.LoginDTO;
import com.example.dto.LoginResponseDTO;
import com.example.dto.ProfileResponseDTO;
import com.example.dto.UserRegistrationDTO;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final ProfileRepository repository;

    private final ResourceBundleService resourceBundleService;

    private final EmailHistoryService emailHistoryService;

    private final MailService mailService;

    public AuthService(ProfileRepository repository, ResourceBundleService resourceBundleService, EmailHistoryService historyService, MailService mailService) {
        this.repository = repository;

        this.resourceBundleService = resourceBundleService;
        this.emailHistoryService = historyService;
        this.mailService = mailService;
    }

    public ProfileResponseDTO registration(UserRegistrationDTO dto, Language language) {
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



        entity.setStatus(ProfileStatus.NOT_ACTIVE);

        entity.setRole(ProfileRole.ROLE_USER);

        repository.save(entity);

        Thread thread = new Thread() {
            @Override
            public synchronized void start() {
                String sb = "Salom qalaysan \n" +
                        "Bu test message" +
                        "Click the link : http://localhost:8080/auth/verification/email/" +
                        JwtUtil.encode(entity.getEmail(), ProfileRole.ROLE_USER);
                mailService.sendEmail(dto.getEmail(), "Complete Registration", sb);

                EmailHistoryEntity emailHistoryEntity = new EmailHistoryEntity();
                emailHistoryEntity.setTo_email(dto.getEmail());
                emailHistoryEntity.setMessage(sb);
                emailHistoryEntity.setCreatedDate(LocalDateTime.now());

                emailHistoryService.create(emailHistoryEntity);
            }
        };
        thread.start();

        return getDTO(entity);

    }

    public LoginResponseDTO login(LoginDTO dto, Language language) {
        Optional<ProfileEntity> optional = repository.findByEmailAndPassword(dto.getEmail(), MD5.md5(dto.getPassword()));
        if (optional.isEmpty()) {
            throw new LoginOrPasswordWrongException(resourceBundleService.getMessage("credential.wrong", language.name()));
        }

        ProfileEntity entity = optional.get();
        if (entity.getStatus().equals(ProfileStatus.BLOCK)) {
            throw new StatusBlockException("Profile status block");
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

    public ProfileResponseDTO getDTO(ProfileEntity entity) {

        ProfileResponseDTO profileDTO = new ProfileResponseDTO();
        profileDTO.setId(entity.getId());
        profileDTO.setName(entity.getName());
        profileDTO.setSurname(entity.getSurname());
        profileDTO.setEmail(entity.getEmail());
        profileDTO.setStatus(entity.getStatus());
        profileDTO.setRole(entity.getRole());


        return profileDTO;
    }


}
