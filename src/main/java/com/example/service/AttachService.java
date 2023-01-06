package com.example.service;

import com.example.dto.AttachResponseDTO;
import com.example.entity.AttachEntity;
import com.example.enums.Language;
import com.example.exp.OriginalFileNameNullException;
import com.example.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.UUID;

@Service
public class AttachService {

    private final AttachRepository repository;

    @Value("${attach.upload.folder}")
    private String attachUploadFolder;

    @Value("${attach.download.url}")
    private String attachDownloadUrl;

    private final ResourceBundleService resourceBundleService;

    public AttachService(AttachRepository repository, ResourceBundleService resourceBundleService) {
        this.repository = repository;
        this.resourceBundleService = resourceBundleService;
    }

    public AttachResponseDTO saveAttach(MultipartFile file, Language language) {
        try {
            String pathFolder = getYmDString();
            File folder = new File(attachUploadFolder+pathFolder);
            if (!folder.exists()) folder.mkdirs();

            String fileName= UUID.randomUUID().toString();
            String extension = getExtension(file.getOriginalFilename(),language);

            byte[] bytes = file.getBytes();
            Path path = Paths.get(attachUploadFolder + pathFolder + "/"+ fileName +"."+extension);
            Files.write(path,bytes);

            AttachEntity entity = new AttachEntity();
            entity.setId(fileName);
            entity.setOriginName(file.getOriginalFilename());
            entity.setType(extension);
            entity.setPath(pathFolder);
            entity.setSize(file.getSize());
            repository.save(entity);


            AttachResponseDTO dto = new AttachResponseDTO();
            dto.setId(entity.getId());
            dto.setUrl(attachDownloadUrl + fileName + "." + extension);
            return dto;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day; // 2022/04/23
    }

    public String getExtension(String fileName, Language language) {
        // mp3/jpg/npg/mp4.....
        if (fileName == null) {
            throw new OriginalFileNameNullException(resourceBundleService.getMessage("file.name.null", language));
        }
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }
}
