package com.example.youtube.controller;

import com.example.youtube.config.security.CustomUserDetails;
import com.example.youtube.dto.video.*;
import com.example.youtube.enums.Language;
import com.example.youtube.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
@Tag(name = "Video Controller", description = "This controller for video")
public class VideoController {
    private final VideoService service;

    public VideoController(VideoService service) {
        this.service = service;
    }


    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Method for create ", description = "This method used to create a video")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody VideoCreateDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        VideoShortInfo result = service.create(dto, user.getId(), language);
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Method for updtea", description = "This method to update a video details")
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody VideoUpdateDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        VideoShortInfo result = service.update(dto, user.getId(), language);
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Method for change", description = "This method for change video status")
    @PutMapping("/change/{id}")
    public ResponseEntity<?> change(@PathVariable("id") String id, @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        Boolean result = service.change(id, user.getId(), language);
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Method for view count ", description = "This method used to increase view count")
    @PutMapping("/view_count/{id}")
    public ResponseEntity<?> increaseViewCount(@PathVariable("id") String id,
                                               @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        Boolean result = service.increaseViewCount(id, user.getId(), language);
        return ResponseEntity.ok(result);

    }


    @PreAuthorize("permitAll()")
    @Operation(summary = "Method for  get videos  by category  ", description = "This method used to get page video by category id")
    @GetMapping("/get_by_category/{c_id}")
    public ResponseEntity<?> getByCategoryId(@PathVariable("c_id") Integer c_id,
                                             @RequestParam(value = "page", defaultValue = "0") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                                             @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {

        Page<VideoShortInfo> result = service.getByCategoryId(c_id, page, size, language);
        return ResponseEntity.ok(result);

    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "Method for search vide", description = "This method used to search vide by title")
    @GetMapping("/search_by_title/{title}")
    public ResponseEntity<?> getByTitle(@PathVariable("title") String title,
                                        @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {

        VideoShortInfo result = service.searchByTitle(title, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "Method for search vide", description = "This method used to search vide by title")
    @GetMapping("/get_by_tag_id/{tag_id}")
    public ResponseEntity<?> getByTagId(@PathVariable("tag_id") Integer tagId,
                                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                                        @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {

        Page<VideoShortInfo> result = service.getByTagId(tagId, page, size, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "Method for search vide", description = "This method used to search vide by title")
    @GetMapping("/get_full_by_id/{video_id}")
    public ResponseEntity<?> getFullInfoById(@PathVariable("video_id") String videoId,
                                             @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();


        VideoFullInfo result = service.getFullInfoById(videoId, user, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Method for search vide", description = "This method used to search vide by title")
    @GetMapping("/get_list")
    public ResponseEntity<?> getList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size,
                                     @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {


        Page<VideoSuperDTO> result = service.getList(page, size, language);
        return ResponseEntity.ok(result);
    }


}

