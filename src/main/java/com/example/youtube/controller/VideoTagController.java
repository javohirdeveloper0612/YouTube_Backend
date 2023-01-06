package com.example.youtube.controller;

import com.example.youtube.config.security.CustomUserDetails;
import com.example.youtube.dto.video.tag.VideoTagDTO;
import com.example.youtube.dto.video.tag.VideoTagInfoDTO;
import com.example.youtube.dto.video.tag.VideoTagResponseDTO;
import com.example.youtube.enums.Language;
import com.example.youtube.service.VideoTagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/video_tag")
public class VideoTagController {

    private final VideoTagService service;

    public VideoTagController(VideoTagService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    @Operation(summary = "Method for add tag to video ", description = "This method used to add tag to video")
    public ResponseEntity<?> add(@Valid @RequestBody VideoTagDTO dto,
                                 @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        VideoTagResponseDTO result = service.add(dto, user.getId(), language);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete")
    @Operation(summary = "Method for delete tag from video ", description = "This method used to delete tag from video")
    public ResponseEntity<?> delete(@Valid @RequestBody VideoTagDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        Boolean result = service.delete(dto, user.getId(), language);

        return ResponseEntity.ok(result);
    }


    @PreAuthorize("permitAll()")
    @GetMapping("/get_by_video_id/{video_id}")
    @Operation(summary = "Method for get list video tag ", description = "This method used to get video tag list by video id")
    public ResponseEntity<?> getByVideoId(@PathVariable("video_id") String videoId,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {

        List<VideoTagInfoDTO> result = service.getByVideoId(videoId, language);
        return ResponseEntity.ok(result);
    }
}
