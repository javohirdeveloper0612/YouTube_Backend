package com.example.controller;

import com.example.config.security.CustomUserDetail;
import com.example.dto.ChannelDTO;
import com.example.enums.Language;
import com.example.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/channel")
public class ChannelController {

    private final ChannelService service ;

    public ChannelController(ChannelService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Method for create channel ", description = "This method used to create channel ")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ChannelDTO dto , @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        log.info("Create channel profileId = " + user.getId() + " " + dto);

        ChannelDTO result = service.create(dto, language);
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasAnyRole('ROLE_USER' , 'ROLE_OWNER')")
    @Operation(summary = "Method for update comment", description = "This method used to update comment")
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ChannelDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        Boolean result = service.update(dto, user.getId(), language);

        return ResponseEntity.ok(result);
    }

    //     3. Update Channel photo ( USER and OWNER)

    @PreAuthorize("hasAnyRole('ROLE_USER' , 'ROLE_OWNER')")
    @Operation(summary = "Method for update comment", description = "This method used to update comment")
    @PutMapping("/updateChanel")
    public ResponseEntity<?> updateChannelPhoto(@RequestBody ChannelDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        Boolean result = service.updateChannelPhoto(dto, user.getId(), language);

        return ResponseEntity.ok(result);
    }

    //    4. Update Channel banner ( USER and OWNER)
    @PreAuthorize("hasAnyRole('ROLE_USER' , 'ROLE_OWNER')")
    @Operation(summary = "Method for update comment", description = "This method used to update comment")
    @PutMapping("/updateBanner")
    public ResponseEntity<?> updateChannelBanner(@RequestBody ChannelDTO dto,
                                                @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        Boolean result = service.updateChannelBanner(dto, user.getId(), language);

        return ResponseEntity.ok(result);
    }

    //  5. Channel Pagination (ADMIN)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Channel get by admin  method", description = "This method used to create email from only admin")
    @PostMapping("/getEmailByAdmin")
    public ResponseEntity<?> getEmailPaginationByAdmin(@RequestParam(value = "page", defaultValue = "0")
                                                           Integer page , @RequestParam(
                                                                   value = "size" , defaultValue = "3") Integer size){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        Page<ChannelDTO> result = service.getPageList(page , size);
        return ResponseEntity.ok(result);
    }

    //   6. Get Channel By Id
    public ResponseEntity<?> getChannelById(@RequestParam String id ,
                                            @RequestHeader(value = "Accept-Language",
                                                    defaultValue = "RU") Language language){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        log.info("Use get Channel  profileId = " + user.getId()  );
        ChannelDTO result = service.findById(id , user.getId() ,language);

        return ResponseEntity.ok(result);
    }
    // 7. Change Channel Status (ADMIN,USER and OWNER)
    @PreAuthorize("hasAnyRole('ROLE_USER' , 'ROLE_OWNER')")
    @Operation(summary = "Method for update comment", description = "This method used to update comment")
    @PutMapping("/updateStatus")
    public ResponseEntity<?> updateChannelStatus(@RequestBody ChannelDTO dto,
                                                @RequestHeader(value = "Accept-Language",
                                                        defaultValue = "RU") Language language) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        Boolean result = service.updateChannelStatus(dto, user.getId(), language);

        return ResponseEntity.ok(result);
    }


}
