package br.com.ourplaces.our_places_api.service;

import br.com.ourplaces.our_places_api.dto.PlaceCreateDTO;
import br.com.ourplaces.our_places_api.dto.PlaceViewDTO;
import br.com.ourplaces.our_places_api.exception.ResourceNotFoundException;
import br.com.ourplaces.our_places_api.mapper.PlaceMapper;
import br.com.ourplaces.our_places_api.model.Couple;
import br.com.ourplaces.our_places_api.model.Place;
import br.com.ourplaces.our_places_api.model.Rating;
import br.com.ourplaces.our_places_api.model.User;
import br.com.ourplaces.our_places_api.repository.CoupleRepository;
import br.com.ourplaces.our_places_api.repository.PlaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final CoupleRepository coupleRepository;
    private final PlaceMapper placeMapper;

    public PlaceService(PlaceRepository placeRepository, CoupleRepository coupleRepository, PlaceMapper placeMapper) {
        this.placeRepository = placeRepository;
        this.coupleRepository = coupleRepository;
        this.placeMapper = placeMapper;
    }

    @Transactional
    public PlaceViewDTO create(PlaceCreateDTO placeCreateDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Couple couple = coupleRepository.findByUser1IdOrUser2Id(currentUser.getId(), currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Couple not found for the authenticated user."));

        Place place = placeMapper.toPlace(placeCreateDTO);
        place.setCouple(couple);

        Rating initialRating = new Rating();
        initialRating.setScore(placeCreateDTO.initialRating().score());
        initialRating.setComment(placeCreateDTO.initialRating().comment());
        initialRating.setUser(currentUser);
        initialRating.setPlace(place);

        place.getRatings().add(initialRating);

        Place savedPlace = placeRepository.save(place);
        return placeMapper.toPlaceViewDTO(savedPlace);
    }
}
