package br.com.ourplaces.our_places_api.controller;

import br.com.ourplaces.our_places_api.dto.CoupleViewDTO;
import br.com.ourplaces.our_places_api.dto.UserCreateDTO;
import br.com.ourplaces.our_places_api.service.CoupleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
