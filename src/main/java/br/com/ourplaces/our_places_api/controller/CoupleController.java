package br.com.ourplaces.our_places_api.controller;

import br.com.ourplaces.our_places_api.dto.CoupleViewDTO;
import br.com.ourplaces.our_places_api.dto.UserCreateDTO;
import br.com.ourplaces.our_places_api.service.CoupleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/couples")
public class CoupleController {
    private final CoupleService coupleService;

    public CoupleController(CoupleService coupleService) {
        this.coupleService = coupleService;
    }

    @PostMapping
    public ResponseEntity<CoupleViewDTO> createCouple(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        CoupleViewDTO createdCouple = coupleService.createCouple(userCreateDTO);
        return new ResponseEntity<>(createdCouple, HttpStatus.CREATED);
    }

    @PostMapping("/{inviteCode}/join")
    public ResponseEntity<CoupleViewDTO> joinCouple(@PathVariable String inviteCode, @Valid @RequestBody UserCreateDTO userCreateDTO) {
        CoupleViewDTO joinedCouple = coupleService.joinCouple(inviteCode, userCreateDTO);
        return new ResponseEntity<>(joinedCouple, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CoupleViewDTO> getCouple() {
        CoupleViewDTO couple = coupleService.getCouple();
        return new ResponseEntity<>(couple, HttpStatus.OK);
    }

    @PostMapping("/picture")
    public ResponseEntity<Void> uploadCouplePicture(@RequestParam("file") MultipartFile file) throws IOException {
        coupleService.updateCouplePicture(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/picture")
    public ResponseEntity<byte[]> getCouplePicture() {
        Map<String, Object> pictureData = coupleService.getCouplePicture();
        byte[] imageBytes = (byte[]) pictureData.get("imageBytes");
        String mediaType = (String) pictureData.get("mediaType");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mediaType))
                .body(imageBytes);
    }
}
