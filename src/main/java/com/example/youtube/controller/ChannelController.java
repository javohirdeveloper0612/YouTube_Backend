package com.example.youtube.controller;

import com.example.youtube.config.security.CustomUserDetails;
import com.example.youtube.dto.channel.ChannelCreateDTO;
import com.example.youtube.dto.channel.ChannelResponseDTO;
import com.example.youtube.dto.channel.ChannelUpdatePropertiesDTO;
import com.example.youtube.enums.Language;
import com.example.youtube.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/channel")
@Tag(name = "Channel Controller", description = "This api used to control channel")
public class ChannelController {
    // Mazgi
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }


    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Channel create method", description = "User use this method to create channel")
    @PostMapping("/create")
    public ResponseEntity<ChannelResponseDTO> create(@Valid @RequestBody ChannelCreateDTO dto) {
        log.info("Channel creating : name = {}, description = {}", dto.getName(), dto.getDescription());
        ChannelResponseDTO result = channelService.create(dto, getUserId());
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Channel update details method", description = "User use this method to update channel properties name, description, ")
    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> updateChannel(@PathVariable String id, @RequestBody ChannelUpdatePropertiesDTO dto,
                                                 @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        log.info("Channel updating : name = {}, description = {}", dto.getName(), dto.getDescription());
        Boolean result = channelService.update(id, dto, getUserId(), language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Channel update avatar method", description = "User use this method to update channel's avatar")
    @PutMapping("/update/photo/{id}")
    public ResponseEntity<Boolean> updateChannelPhoto(@PathVariable String id, @RequestParam("file") MultipartFile file,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        log.info("Updating {} channel photo: ", id);
        Boolean result = channelService.updatePhoto(id, file, getUserId(), language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Channel update banner method", description = "User use this method to update channel's banner")
    @PutMapping("/update/banner/{id}")
    public ResponseEntity<Boolean> updateChannelBanner(@PathVariable String id, @RequestParam("file") MultipartFile file,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        log.info("Updating {} channel banner: ", id);
        Boolean result = channelService.updateBanner(id, file, getUserId(), language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Channel List Pagination", description = "Get Channel List Pagination Only Admins")
    @GetMapping("/list")
    public ResponseEntity<Page<ChannelResponseDTO>> getList(@RequestParam("page") Integer page,
                                                            @RequestParam("size") Integer size) {

        Page<ChannelResponseDTO> result = channelService.getList(page, size);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "Channel with id", description = "Get channel with id")
    @GetMapping("/get/{id}")
    public ResponseEntity<ChannelResponseDTO> getById(@PathVariable String id,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        ChannelResponseDTO result = channelService.getById(id, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "User channel list with Pagination", description = "Get channel list with Pagination owned by current user")
    @GetMapping("/my/channels")
    public ResponseEntity<Page<ChannelResponseDTO>> getUserChannelsList(@RequestParam("page") Integer page,
                                                                        @RequestParam("size") Integer size) {
        Page<ChannelResponseDTO> result = channelService.getUserChannelsList(page, size, getUserId());
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Change Channel Status", description = "Method for Change Channel status")
    @PutMapping("/update/status/{id}")
    public ResponseEntity<Boolean> changeChannelStatus(@PathVariable String id,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        Boolean result = channelService.changeStatus(id, getUserId(), language);
        return ResponseEntity.ok(result);
    }

    private Integer getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return user.getId();
    }
}
