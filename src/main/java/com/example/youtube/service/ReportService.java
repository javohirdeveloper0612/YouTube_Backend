package com.example.youtube.service;

import com.example.youtube.dto.ReportDTO;
import com.example.youtube.dto.attach.PreviewAttachDTO;
import com.example.youtube.dto.profile.ProfileInfoDTO;
import com.example.youtube.dto.report.ReportInfo;
import com.example.youtube.entity.AttachEntity;
import com.example.youtube.entity.CategoryEntity;
import com.example.youtube.entity.ProfileEntity;
import com.example.youtube.entity.ReportEntity;
import com.example.youtube.enums.Language;
import com.example.youtube.enums.ReposrtType;
import com.example.youtube.exp.CategoryNotFoundException;
import com.example.youtube.exp.ReportAlReadException;
import com.example.youtube.exp.ReportNotFoundException;
import com.example.youtube.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    @Autowired
    private ReportRepository repo;

    @Autowired
    private ResourceBundleService resourceBundleService;

    @Value("${attach.download.url}")
    private String attachDownloadUrl;

    public Page<ReportDTO> getAllFromDb(int page, int size) {

        List<ReportDTO> list = new LinkedList<>();

        Pageable pageable = PageRequest.of(page, size);

        Page<ReportEntity> page1 = repo.findAll(pageable);


        for (ReportEntity report : page1.getContent()) {
            ReportDTO dto = new ReportDTO();
            dto.setId(report.getId());
            dto.setProfileId(report.getProfileId());
            dto.setContent(report.getContent());
            dto.setChannleId(report.getEntityId());
            dto.setType(report.getType());

            list.add(dto);
        }


        return new PageImpl<>(list, pageable, page1.getTotalElements());
    }


    public ReportEntity getById(Integer id, Language language) {
        Optional<ReportEntity> optional = repo.findById(id);

        if (optional.isEmpty()) {
            throw new ReportNotFoundException(resourceBundleService.getMessage("report.not.found", language));
        }
        return optional.get();
    }

    public Boolean deleteById(Integer id, Language language) {
        getById(id, language);

        repo.deleteById(id);
        return true;
    }

    public String add(ReportDTO dto, Language language) {

        Optional<ReportEntity> entity = repo.findById(dto.getProfileId());

        if (entity != null) {
            throw new ReportAlReadException(resourceBundleService.getMessage("report.alRead", language));
        }

        ReportEntity entity1 = new ReportEntity();
        dto.setId(entity1.getId());
        dto.setContent(entity1.getContent());
        dto.setType(ReposrtType.CHANNEL);


        repo.save(entity1);
        return "add";
    }

    public ReportDTO edit(ReportDTO dto, Integer id, Language language) {
        Optional<ReportEntity> optional = repo.findById(id);
        if (optional.isEmpty()) {
            throw new ReportNotFoundException(resourceBundleService.getMessage("report.not.found", language));
        }


        ReportEntity entity = new ReportEntity();
        entity.setContent(dto.getContent());


        repo.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public List<ReportInfo> getListByUserId(Integer id) {

        List<ReportInfo> list = new LinkedList<>();

        for (ReportEntity report : repo.findByProfileId(id)) {
            list.add(getReportInfo(report));
        }
        return list;
    }

    private ReportInfo getReportInfo(ReportEntity entity) {
        ReportInfo reportInfo = new ReportInfo();

        reportInfo.setId(entity.getId());
        reportInfo.setProfile(getProfileInfo(entity.getProfile()));
        return reportInfo;
    }

    private ProfileInfoDTO getProfileInfo(ProfileEntity entity) {

        ProfileInfoDTO profileInfo = new ProfileInfoDTO();


        profileInfo.setId(entity.getId());
        profileInfo.setName(entity.getName());
        profileInfo.setSurname(entity.getSurname());
        profileInfo.setPhoto(getPhoto(entity.getPhoto()));
        return profileInfo;

    }

    private PreviewAttachDTO getPhoto(AttachEntity entity) {

        PreviewAttachDTO preview = new PreviewAttachDTO();

        preview.setId(entity.getId());
        preview.setUrl(attachDownloadUrl + entity.getId() + "." + entity.getType());

        return preview;
    }
}
