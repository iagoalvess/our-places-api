package br.com.ourplaces.our_places_api.controller;

import br.com.ourplaces.our_places_api.dto.CoupleViewDTO;
import br.com.ourplaces.our_places_api.dto.UserCreateDTO;
import br.com.ourplaces.our_places_api.exception.ResourceNotFoundException;
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
    public ResponseEntity<?> uploadCouplePicture(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File not sent.");
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("File too large. Maximum 5MB.");
            }

            coupleService.updateCouplePicture(file);
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing file.");

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
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
