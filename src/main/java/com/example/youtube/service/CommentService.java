package com.example.youtube.service;

import com.example.youtube.dto.attach.PreviewAttachDTO;
import com.example.youtube.dto.comment.*;
import com.example.youtube.dto.profile.ProfileInfoDTO;
import com.example.youtube.dto.video.VideoExtraShortDTO;
import com.example.youtube.entity.AttachEntity;
import com.example.youtube.entity.CommentEntity;
import com.example.youtube.entity.ProfileEntity;
import com.example.youtube.entity.VideoEntity;
import com.example.youtube.enums.Language;
import com.example.youtube.enums.ProfileRole;
import com.example.youtube.exp.SomethingWentWrong;
import com.example.youtube.exp.comment.CommentNoAccessException;
import com.example.youtube.exp.comment.CommentNotFoundException;
import com.example.youtube.repository.CommentRepository;
import com.example.youtube.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Value("${attach.download.url}")
    private String attachDownloadUrl;
    private final CommentRepository commentRepository;

    private final ProfileRepository profileRepository;

    private final ResourceBundleService resourceBundleService;

    public CommentService(CommentRepository commentRepository, ProfileRepository profileRepository, ResourceBundleService resourceBundleService) {
        this.commentRepository = commentRepository;
        this.profileRepository = profileRepository;
        this.resourceBundleService = resourceBundleService;
    }

    public CommentResponseDTO create(CommentCreateDTO dto, Integer userId) {
        CommentEntity comment = new CommentEntity();
        comment.setVideoId(dto.getVideoId());
        comment.setProfileId(userId);
        comment.setContent(dto.getContent());
        comment.setCreatedDate(LocalDateTime.now());
        commentRepository.save(comment);

        CommentResponseDTO response = new CommentResponseDTO();
        response.setId(comment.getId());
        response.setProfileId(comment.getProfileId());
        response.setVideoId(comment.getVideoId());
        response.setContent(comment.getContent());
        return response;
    }

    public Boolean updateById(Integer id, Integer profileId, CommentUpdateDTO dto, Language language) {
        Optional<CommentEntity> byId = commentRepository.findById(id);
        if (byId.isEmpty()) {
            throw new CommentNotFoundException(resourceBundleService.getMessage("comment.not.found", language));
        }

        CommentEntity comment = byId.get();
        if (!comment.getVideoId().equals(dto.getVideoId())) {
            throw new SomethingWentWrong(resourceBundleService.getMessage("smth.wrong", language));
        }

        if (comment.getProfileId() != profileId) {
            throw new CommentNoAccessException(resourceBundleService.getMessage("comment.no.access", language));
        }

        comment.setContent(dto.getContent());
        commentRepository.save(comment);
        return true;
    }

    public Boolean deleteById(Integer id, Integer profileId, Language language) {
        Optional<CommentEntity> byId = commentRepository.findById(id);
        if (byId.isEmpty()) {
            throw new CommentNotFoundException(resourceBundleService.getMessage("comment.not.found", language));
        }

        CommentEntity comment = byId.get();

        if (!comment.getProfileId().equals(profileId)) {
            Optional<ProfileEntity> user = profileRepository.findById(profileId);

            if (user.get().getRole() != ProfileRole.ROLE_ADMIN) {
                throw new CommentNoAccessException(resourceBundleService.getMessage("comment.no.access", language));
            }

            commentRepository.delete(comment);
        }
        commentRepository.delete(comment);
        return true;
    }

    public Page<CommentDTO> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<CommentEntity> all = commentRepository.findAll(pageable);

        List<CommentEntity> content = all.getContent();

        List<CommentDTO> result = new ArrayList<>();

        for (CommentEntity comment : content) {
            CommentDTO response = new CommentDTO();
            response.setId(comment.getId());
            response.setProfileId(comment.getProfileId());
            response.setVideoId(comment.getVideoId());
            response.setContent(comment.getContent());
            response.setReplyId(comment.getReplyId());
            response.setLikeCount(comment.getLikeCount());
            response.setDislikeCount(comment.getDislikeCount());

            result.add(response);
        }

        return new PageImpl<>(result, pageable, all.getTotalElements());
    }

    public List<CommentFullResponseDTO> getByProfileId(Integer profileId) {
        List<CommentEntity> byProfileId = commentRepository.findByProfileId(profileId);

        List<CommentFullResponseDTO> result = new LinkedList<>();
        for (CommentEntity comment : byProfileId) {
            result.add(getFullResponse(comment));
        }
        return result;
    }

    public List<CommentInfoDTO> getByVideoId(String id) {
        List<CommentEntity> byVideoId = commentRepository.findByVideoId(id);

        List<CommentInfoDTO> result = new LinkedList<>();
        for (CommentEntity comment : byVideoId) {
            result.add(getCommentInfo(comment));
        }
        return result;
    }


    // hozircha bitta reply bor deb olaylik agar replylar ko'p bolsa aloxida table ochish kerak
    public CommentInfoDTO getRepliedCommentsById(Integer id) {
        Optional<CommentEntity> byVideoId = commentRepository.findById(id);
        CommentEntity reply = byVideoId.get().getReply();

        CommentInfoDTO result = getCommentInfo(reply);
        return result;
    }

    private CommentInfoDTO getCommentInfo(CommentEntity comment) {
        CommentInfoDTO dto = new CommentInfoDTO();
        if (comment != null) {
            dto.setId(comment.getId());
            dto.setContent(comment.getContent());
            dto.setCreatedDate(comment.getCreatedDate());
            dto.setLikeCount(comment.getLikeCount());
            dto.setDislikeCount(comment.getDislikeCount());
            dto.setProfile(getProfileInfo(comment.getProfile()));
        }

        return dto;
    }

    private ProfileInfoDTO getProfileInfo(ProfileEntity profile) {
        ProfileInfoDTO dto = new ProfileInfoDTO();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setPhoto(getPreviewAttach(profile.getPhoto()));

        return dto;
    }

    private CommentFullResponseDTO getFullResponse(CommentEntity comment) {
        CommentFullResponseDTO dto = new CommentFullResponseDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setReplyId(comment.getReplyId());
        dto.setLikeCount(comment.getLikeCount());
        dto.setDislikeCount(comment.getDislikeCount());
        dto.setVideo(getExtraShortVideo(comment.getVideo()));
        return dto;
    }

    private VideoExtraShortDTO getExtraShortVideo(VideoEntity video) {
        VideoExtraShortDTO dto = new VideoExtraShortDTO();
        dto.setId(video.getId());
        dto.setTitle(video.getTitle());
        dto.setDuration(video.getDuration());
        dto.setPreviewAttach(getPreviewAttach(video.getPreviewAttach()));

        return dto;
    }

    private PreviewAttachDTO getPreviewAttach(AttachEntity previewAttach) {
        PreviewAttachDTO dto = new PreviewAttachDTO();
        if (previewAttach != null) {
            dto.setId(previewAttach.getId());
            dto.setUrl(attachDownloadUrl + previewAttach.getId() + "." + previewAttach.getType());
        }

        return dto;
    }

}
