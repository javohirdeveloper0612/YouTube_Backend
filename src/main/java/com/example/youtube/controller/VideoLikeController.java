package com.example.youtube.controller;

import com.example.youtube.config.security.CustomUserDetails;
import com.example.youtube.dto.videoLike.VideoLikeCreateDTO;
import com.example.youtube.dto.videoLike.VideoLikeInfo;
import com.example.youtube.dto.videoLike.VideoLikeResponseDTO;
import com.example.youtube.enums.Language;
import com.example.youtube.service.VideoLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/video/like")
@Tag(name = "Video Like Controller", description = "This controller to like video")
public class VideoLikeController {
    @Autowired
    private VideoLikeService videoLikeService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Method for create like ", description = "This method is used to like a video")
    @PostMapping("/create")
    public ResponseEntity<VideoLikeResponseDTO> create(@Valid @RequestBody VideoLikeCreateDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        log.info("{} is creating a like", getUserId());
        VideoLikeResponseDTO result = videoLikeService.create(dto, getUserId(), language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Method for delete like", description = " This method is used to delete like ")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        log.info("Like is deleting: {}", id);
        Boolean result = videoLikeService.deleteById(id, getUserId(), language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Method for get liked videos", description = "This method is used to get all liked videos")
    @GetMapping("/my/liked")
    public ResponseEntity<List<VideoLikeInfo>> getLikedVideos() {
        List<VideoLikeInfo> result = videoLikeService.getLikedVideos(getUserId());
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Method for get liked videos", description = "This method is used to get all liked videos")
    @GetMapping("/user/liked/{id}")
    public ResponseEntity<List<VideoLikeInfo>> getUserLiked(@PathVariable Integer id) {
        System.out.println("Method is working");
        List<VideoLikeInfo> result = videoLikeService.getLikedVideos(id);
        return ResponseEntity.ok(result);
    }

    private Integer getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return user.getId();
    }
}
