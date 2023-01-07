package com.example.youtube.service;

import com.example.youtube.dto.attach.AttachResponseDTO;
import com.example.youtube.dto.profile.ProfileResponseDTO;
import com.example.youtube.dto.auth.AdminRegistrationDTO;
import com.example.youtube.dto.profile.ProfileDetailDTO;
import com.example.youtube.entity.EmailHistoryEntity;
import com.example.youtube.entity.ProfileEntity;
import com.example.youtube.enums.Language;
import com.example.youtube.enums.ProfileRole;
import com.example.youtube.enums.ProfileStatus;
import com.example.youtube.exp.EmailAlreadyExistsException;
import com.example.youtube.exp.FileTypeIncorrectException;
import com.example.youtube.exp.LimitOutPutException;
import com.example.youtube.exp.ProfileNotFoundException;
import com.example.youtube.repository.ProfileRepository;
import com.example.youtube.util.JwtUtil;
import com.example.youtube.util.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class ProfileService {

    private final ProfileRepository repository;
    private final ResourceBundleService resourceBundleService;
    private final AttachService attachService;
    private final EmailHistoryService emailHistoryService;
    private final MailService mailService;
@Lazy
    public ProfileService(ProfileRepository repository, ResourceBundleService resourceBundleService, AttachService attachService, EmailHistoryService emailHistoryService, MailService mailService) {
        this.repository = repository;
        this.resourceBundleService = resourceBundleService;
        this.attachService = attachService;
        this.emailHistoryService = emailHistoryService;
        this.mailService = mailService;
    }


    public ProfileResponseDTO updatePassword(String password, Integer id, Language language) {
        Optional<ProfileEntity> optional = repository.findById(id);

        if (optional.isEmpty()) {
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language));
        }

        ProfileEntity entity = optional.get();
        entity.setPassword(MD5.md5(password));

        repository.save(entity);

        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());

        return dto;

    }

    public ProfileResponseDTO updateEmail(String email, Integer id, Language language) {
        Optional<ProfileEntity> exists = repository.findByEmail(email);
        if (exists.isPresent()) {
            ProfileEntity entity = exists.get();
            if (entity.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
                repository.delete(entity);
            } else {
                throw new EmailAlreadyExistsException(resourceBundleService.getMessage("email.exists", language));
            }
        }

        Long countInMinute = emailHistoryService.getCountInMinute(email);
        if (countInMinute > 4) {
            throw new LimitOutPutException(resourceBundleService.getMessage("resent.limit", language));
        }


        Optional<ProfileEntity> optional = repository.findById(id);

        if (optional.isEmpty()) {
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language));
        }


        ProfileEntity entity = optional.get();
        entity.setEmail(email);
        entity.setStatus(ProfileStatus.NOT_ACTIVE);
        repository.save(entity);


        Thread thread = new Thread() {
            @Override
            public synchronized void start() {
                String sb = "Salom qalaysan \n" +
                        "Bu test message" +
                        "Click the link : http://localhost:8080/auth/verification/email/" +
                        JwtUtil.encode(entity.getEmail(), ProfileRole.ROLE_USER);
                mailService.sendEmail(email, "Complete Registration", sb);

                EmailHistoryEntity emailHistoryEntity = new EmailHistoryEntity();
                emailHistoryEntity.setEmail(email);
                emailHistoryEntity.setMessage(sb);
                emailHistoryEntity.setCreatedDate(LocalDateTime.now());

                emailHistoryService.create(emailHistoryEntity);
            }
        };
        thread.start();


        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());

        return dto;
    }

    public ProfileResponseDTO updateDetail(ProfileDetailDTO detailDTO, Integer id, Language language) {
        Optional<ProfileEntity> optional = repository.findById(id);

        if (optional.isEmpty()) {
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language));
        }

        ProfileEntity entity = optional.get();
        entity.setName(detailDTO.getName());
        entity.setSurname(detailDTO.getSurname());

        repository.save(entity);

        ProfileResponseDTO responseDTO = new ProfileResponseDTO();
        responseDTO.setEmail(entity.getEmail());
        responseDTO.setName(entity.getName());
        responseDTO.setSurname(entity.getSurname());

        return responseDTO;

    }

    public ProfileResponseDTO updatePhoto(Integer id, MultipartFile file, Language language) {

        String extension = attachService.getExtension(file.getOriginalFilename(), language);

        if (!extension.equalsIgnoreCase("png") && !extension.equalsIgnoreCase("jpg")) {
            throw new FileTypeIncorrectException(resourceBundleService.getMessage("file.type.incorrect", language));
        }


        Optional<ProfileEntity> optional = repository.findById(id);

        if (optional.isEmpty()) {
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language));
        }

        ProfileEntity entity = optional.get();

        AttachResponseDTO attachResponseDTO = attachService.saveToSystem(file, language);

        entity.setPhotoId(attachResponseDTO.getId());

        repository.save(entity);

        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setPhotoId(entity.getPhotoId());
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());
        return dto;
    }

    public ProfileResponseDTO getDetail(Integer id, Language language) {
        Optional<ProfileEntity> optional = repository.findById(id);

        if (optional.isEmpty()) {
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language));
        }

        ProfileEntity entity = optional.get();

        ProfileResponseDTO responseDTO = new ProfileResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setPhotoId(attachService.getUrl(entity.getPhotoId(), language));
        responseDTO.setEmail(entity.getEmail());
        responseDTO.setName(entity.getName());
        responseDTO.setSurname(entity.getSurname());
        return responseDTO;
    }

    private void checkEmail(String email) {

        Optional<ProfileEntity> exists = repository.findByEmail(email);
        if (exists.isPresent()) {
            ProfileEntity entity = exists.get();
            if (entity.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
                repository.delete(entity);
            } else {
                throw new EmailAlreadyExistsException("Email already exists");
            }
        }
    }


    public ProfileResponseDTO create(AdminRegistrationDTO dto) {

        checkEmail(dto.getEmail());

        ProfileEntity entity = new ProfileEntity();
        entity.setRole(dto.getRole());

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5.md5(dto.getPassword()));

        entity.setStatus(ProfileStatus.NOT_ACTIVE);


        repository.save(entity);

        Thread thread = new Thread() {
            @Override
            public synchronized void start() {
                String sb = "Salom qalaysan \n" +
                        "Bu test message" +
                        "Click the link : http://localhost:8080/auth/verification/email/" +
                        JwtUtil.encode(entity.getEmail(), entity.getRole());
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

    private ProfileResponseDTO getDTO(ProfileEntity entity) {

        return new ProfileResponseDTO(entity.getId(),
                entity.getName(), entity.getSurname(),
                entity.getStatus(), entity.getRole(), entity.getVisible(), entity.getCreatedDate());
    }

}
