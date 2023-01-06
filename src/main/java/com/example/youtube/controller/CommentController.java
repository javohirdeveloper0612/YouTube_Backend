package com.example.youtube.controller;

import com.example.youtube.config.security.CustomUserDetails;
import com.example.youtube.dto.comment.*;
import com.example.youtube.enums.Language;
import com.example.youtube.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comment")
@Tag(name = "Comment Controller", description = "This api used to control comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Comment create method", description = "User use this method to create comment")
    @PostMapping("/create")
    public ResponseEntity<CommentResponseDTO> create(@Valid @RequestBody CommentCreateDTO dto) {
        log.info("Comment creating : comment = {}, video = {}", dto.getContent(), dto.getVideoId());
        CommentResponseDTO result = commentService.create(dto, getUserId());
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Comment update method", description = "User use this method to update comment")
    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Integer id, @RequestBody CommentUpdateDTO dto,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        log.info("Comment updating : comment = {}, video = {}", dto.getContent(), dto.getVideoId());
        Boolean result = commentService.updateById(id, getUserId(), dto, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Comment delete method", description = "User use this method to delete comment")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        log.info("Comment deleting : comment = {}", id);
        Boolean result = commentService.deleteById(id, getUserId(), language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Comment List With Pagination", description = "ADMIN use this method to get comment pagination")
    @GetMapping("/get")
    public ResponseEntity<Page<CommentDTO>> getAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<CommentDTO> result = commentService.getAll(page, size);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get comments by user id", description = "ADMIN use this method to get comment")
    @GetMapping("/get/user/{id}")
    public ResponseEntity<List<CommentFullResponseDTO>> getCommentsByUserId(@PathVariable Integer id) {
        List<CommentFullResponseDTO> result = commentService.getByProfileId(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get owner comments", description = "User use this method to get comment")
    @GetMapping("/my/comments/")
    public ResponseEntity<List<CommentFullResponseDTO>> getOwnerComments() {
        List<CommentFullResponseDTO> result = commentService.getByProfileId(getUserId());
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get comment by video id", description = "User use this method to get comment by video id")
    @GetMapping("/by/video/{id}")
    public ResponseEntity<List<CommentInfoDTO>> getCommentsByVideoId(@PathVariable String id) {
        List<CommentInfoDTO> result = commentService.getByVideoId(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get comment replies", description = "User use this method to get comment replies")
    @GetMapping("/replies/by/comment/{id}")
    public ResponseEntity<CommentInfoDTO> getRepliedComments(@PathVariable Integer id) {
        CommentInfoDTO result = commentService.getRepliedCommentsById(id);
        return ResponseEntity.ok(result);
    }

    private Integer getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return user.getId();
    }
}
