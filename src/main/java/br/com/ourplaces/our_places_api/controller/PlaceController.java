package br.com.ourplaces.our_places_api.controller;

import br.com.ourplaces.our_places_api.dto.PlaceCreateDTO;
import br.com.ourplaces.our_places_api.dto.PlaceViewDTO;
import br.com.ourplaces.our_places_api.dto.RatingDTO;
import br.com.ourplaces.our_places_api.service.PlaceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<PlaceViewDTO>> getAllPlaces() {
        List<PlaceViewDTO> places = placeService.findAllByCouple();
        return new ResponseEntity<>(places, HttpStatus.OK);
    }

    @PostMapping("/{places}/ratings")
    public ResponseEntity<PlaceViewDTO> addRating(@PathVariable Long places, @Valid @RequestBody RatingDTO ratingDTO) {
        PlaceViewDTO updatedPlace = placeService.addRating(places, ratingDTO);
        return new ResponseEntity<>(updatedPlace, HttpStatus.OK);
    }
}
