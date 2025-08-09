package br.com.ourplaces.our_places_api.controller;

import br.com.ourplaces.our_places_api.dto.CoupleViewDTO;
import br.com.ourplaces.our_places_api.dto.ImportantDateDTO;
import br.com.ourplaces.our_places_api.service.CoupleService;
import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/couples/important-dates")
public class ImportantDateController {
    private final CoupleService coupleService;

    public ImportantDateController(CoupleService coupleService) {
        this.coupleService = coupleService;
    }

    @PostMapping
    public ResponseEntity<CoupleViewDTO> addImportantDate(@Valid @RequestBody ImportantDateDTO importantDateDTO) {
        CoupleViewDTO coupleViewDTO = coupleService.addImportantDate(importantDateDTO);
        return new ResponseEntity<>(coupleViewDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ImportantDateDTO>> getImportantDates() {
        List<ImportantDateDTO> dates = coupleService.getImportantDates();
        return ResponseEntity.ok(dates);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImportantDate(
            @RequestParam String description,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate date) {
        coupleService.removeImportantDate(description, date);
        return ResponseEntity.noContent().build();
    }
}
