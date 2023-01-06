package com.example.youtube.controller;

import com.example.youtube.dto.ReportDTO;
import com.example.youtube.dto.report.ReportInfo;
import com.example.youtube.entity.CategoryEntity;
import com.example.youtube.entity.ReportEntity;
import com.example.youtube.enums.Language;
import com.example.youtube.service.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@Tag(name = "Report",description ="thes is Control Report")
public class ReportController {


    @Autowired
    private ReportService service;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(name = "page", defaultValue = "1") int page,
                                @RequestParam(name = "size", defaultValue = "1") int size){
        Page<ReportDTO> allReportFromDb = service.getAllFromDb(page, size);

        return ResponseEntity.ok(allReportFromDb);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody ReportDTO dto,
                             @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language){
        String result =service.add(dto,language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@Valid @RequestBody ReportDTO  dto,@PathVariable Integer id,
                              @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language){
        ReportDTO result= service.edit(dto,id,language);
        return ResponseEntity.ok(result);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id,
                                 @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language){
        ReportEntity ReportById =service.getById(id,language);
        return ResponseEntity.ok(ReportById);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language){
        Boolean result =service.deleteById(id,language);
        return ResponseEntity.ok(result);

    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getListByUserId(@PathVariable Integer id){
        List<ReportInfo> result=service.getListByUserId(id);
        return ResponseEntity.ok(result);
    }
}
