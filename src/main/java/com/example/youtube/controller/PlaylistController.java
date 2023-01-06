package com.example.youtube.controller;

import com.example.youtube.config.security.CustomUserDetails;
import com.example.youtube.dto.channel.ChannelCreateDTO;
import com.example.youtube.dto.channel.ChannelResponseDTO;
import com.example.youtube.dto.channel.ChannelUpdatePropertiesDTO;
import com.example.youtube.dto.playlist.*;
import com.example.youtube.enums.Language;
import com.example.youtube.service.PlaylistService;
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
@RequestMapping("/playlist")
@Tag(name = "Playlist Controller", description = "This api used to control channel")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;


    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Playlist create method", description = "User use this method to create playlist")
    @PostMapping("/create")
    public ResponseEntity<PlaylistResponseDTO> create(@Valid @RequestBody PlaylistCreateDTO dto,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        log.info("Playlist creating : name = {}, description = {}", dto.getName(), dto.getDescription());
        PlaylistResponseDTO result = playlistService.create(dto, getUserId(), language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Playlist update details method", description = "User use this method to update playlist properties name, description, ")
    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> updateChannel(@PathVariable Integer id, @Valid @RequestBody PlaylistUpdateDTO dto,
                                                 @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        log.info("Playlist updating : name = {}, description = {}", dto.getName(), dto.getDescription());
        Boolean result = playlistService.update(id, dto, getUserId(), language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "Change Playlist Status", description = "When you use this method if playlist status is public it will be private or vice-versa")
    @PutMapping("/update/status/{id}")
    public ResponseEntity<Boolean> changeChannelStatus(@PathVariable Integer id,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        Boolean result = playlistService.changeStatus(id, getUserId(), language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Playlist delete method", description = " OWNER or ADMIN delete Playlist via this method ")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        log.info("Tag is deleting: {}", id);
        Boolean result = playlistService.deleteById(id, getUserId(), language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Playlist Pagination", description = "Get PlayList Pagination Only Admins")
    @GetMapping("/get")
    public ResponseEntity<Page<PlayListInfoDTO>> getList(@RequestParam("page") Integer page,
                                                            @RequestParam("size") Integer size) {

        Page<PlayListInfoDTO> result = playlistService.getList(page, size);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Playlist by User Id", description = "Get PlayList By User Id Only Admins")
    @GetMapping("/by/user/{id}")
    public ResponseEntity<List<PlayListInfoDTO>> getListByUserId(@PathVariable Integer id) {
        List<PlayListInfoDTO> result = playlistService.getListByUserId(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Playlist by User Id", description = "Get PlayList By User Id Only Admins")
    @GetMapping("/my/playlist")
    public ResponseEntity<List<PlayListShortInfoDTO>> getUserPlaylist() {
        List<PlayListShortInfoDTO> result = playlistService.getShortListByUserId(getUserId());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Playlist by ChannelId", description = "Get PlayList By Channel Id")
    @GetMapping("/get/by/channel/{id}")
    public ResponseEntity<List<PlayListShortInfoDTO>> getByChannelId(@PathVariable String id) {
        List<PlayListShortInfoDTO> result = playlistService.getPlaylistByChannelId(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Playlist by User Id", description = "Get PlayList By User Id Only Admins")
    @GetMapping("/get/{id}")
    public ResponseEntity<PlaylistSmallInfoDTO> getByPlaylistId(@PathVariable Integer id) {
        PlaylistSmallInfoDTO result = playlistService.getByPlaylistId(id);
        return ResponseEntity.ok(result);
    }



    private Integer getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return user.getId();
    }

}
