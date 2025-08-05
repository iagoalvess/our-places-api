package br.com.ourplaces.our_places_api.controller;

import br.com.ourplaces.our_places_api.dto.PlaceCreateDTO;
import br.com.ourplaces.our_places_api.dto.PlaceViewDTO;
import br.com.ourplaces.our_places_api.service.PlaceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/places")
public class PlaceController {
    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @PostMapping
    public ResponseEntity<PlaceViewDTO> createPlace(@Valid @RequestBody PlaceCreateDTO placeCreateDTO) {
        PlaceViewDTO createdPlace = placeService.create(placeCreateDTO);
        return new ResponseEntity<>(createdPlace, HttpStatus.CREATED);
    }
}
