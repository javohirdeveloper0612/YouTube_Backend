package com.example.service;

import com.example.dto.AdminRegistrationDTO;
import com.example.dto.AttachResponseDTO;
import com.example.dto.ProfileDetailDTO;
import com.example.dto.ProfileResponseDTO;
import com.example.entity.EmailHistoryEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.EmailAlreadyExistsException;
import com.example.exp.profile.FileTypeIncorrectException;
import com.example.exp.profile.ProfileNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.util.JwtUtil;
import com.example.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository repository;

    private final ResourceBundleService resourceBundleService;

    private final MailService mailService;

    private final EmailHistoryService emailHistoryService;

    private final AttachService attachService;

    public ProfileService(ProfileRepository repository, ResourceBundleService resourceBundleService, MailService mailService, EmailHistoryService emailHistoryService, AttachService attachService) {
        this.repository = repository;
        this.resourceBundleService = resourceBundleService;
        this.mailService = mailService;
        this.emailHistoryService = emailHistoryService;
        this.attachService = attachService;
    }

    public ProfileResponseDTO updatePassword(String password, Integer id, Language language) {

        Optional<ProfileEntity> optional =repository.findById(id);

        if (optional.isEmpty()){
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found",language));
        }

        ProfileEntity entity = optional.get();
        entity.setPassword(MD5.md5(password));

        repository.save(entity);

        ProfileResponseDTO dto = new ProfileResponseDTO(entity.getId(), entity.getName(), entity.getSurname(), entity.getStatus(), entity.getRole(), entity.getVisible(), entity.getCreatedDate());
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());

        return dto;
    }


    public ProfileResponseDTO updateEmail(String email, Integer id, Language language) {
        Optional<ProfileEntity> optional = repository.findById(id);
        if (optional.isPresent()){
            ProfileEntity entity = optional.get();
            if (entity.getStatus().equals(ProfileStatus.NOT_ACTIVE)){
                repository.delete(entity);
            }else {
                throw new EmailAlreadyExistsException(resourceBundleService.getMessage("email.exists",language));
            }
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


        ProfileResponseDTO dto = new ProfileResponseDTO(entity.getId(), entity.getName(), entity.getSurname(), entity.getStatus(), entity.getRole(), entity.getVisible(), entity.getCreatedDate());
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());

        return dto;

    }

    public ProfileResponseDTO updateDetail(ProfileDetailDTO dto, Integer id, Language language) {
        Optional<ProfileEntity> optional = repository.findById(id);
        if (optional.isEmpty()){
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found",language));
        }

        ProfileEntity entity = optional.get();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());

        repository.save(entity);

        ProfileResponseDTO responseDTO = new ProfileResponseDTO(entity.getId(), entity.getName(), entity.getSurname(), entity.getStatus(), entity.getRole(), entity.getVisible(), entity.getCreatedDate());
        responseDTO.setEmail(entity.getEmail());
        responseDTO.setName(entity.getName());
        responseDTO.setSurname(entity.getSurname());

    return responseDTO;
    }

    public ProfileResponseDTO updatePhoto(Integer id, MultipartFile file, Language language) {
        String extension = attachService.getExtension(file.getOriginalFilename(),language);

        if (!extension.equalsIgnoreCase("png") || !extension.equalsIgnoreCase("jpg")){
            throw new FileTypeIncorrectException(resourceBundleService.getMessage("file.type.incorrect",language));
        }

        Optional<ProfileEntity> optional = repository.findById(id);
        if (optional.isEmpty()){
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found",language));
        }

        ProfileEntity entity = optional.get();

        AttachResponseDTO attachDTO = attachService.saveToSystem(file,language);

        entity.setPhotoId(attachDTO.getId());

        repository.save(entity);
        ProfileResponseDTO dto = new ProfileResponseDTO(entity.getId(), entity.getName(), entity.getSurname(), entity.getStatus(), entity.getRole(), entity.getVisible(), entity.getCreatedDate());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhotoId(entity.getPhotoId());

        return dto;
    }


    public ProfileResponseDTO getDetail(Integer id, Language language) {
        Optional<ProfileEntity > optional = repository.findById(id);

        if (optional.isEmpty()){
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found",language));
        }

        ProfileEntity entity = optional.get();

        ProfileResponseDTO dto = new ProfileResponseDTO(entity.getId(), entity.getName(), entity.getSurname(), entity.getStatus(), entity.getRole(), entity.getVisible(), entity.getCreatedDate());
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        dto.setPhotoId(entity.getPhotoId());
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        return dto;
    }

    public ProfileResponseDTO create(AdminRegistrationDTO dto, Language language) {

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
}
