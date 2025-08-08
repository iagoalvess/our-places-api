package br.com.ourplaces.our_places_api.service;

import br.com.ourplaces.our_places_api.dto.PlaceCreateDTO;
import br.com.ourplaces.our_places_api.dto.PlaceViewDTO;
import br.com.ourplaces.our_places_api.dto.RatingDTO;
import br.com.ourplaces.our_places_api.exception.ResourceNotFoundException;
import br.com.ourplaces.our_places_api.exception.UserAlreadyRatedException;
import br.com.ourplaces.our_places_api.mapper.PlaceMapper;
import br.com.ourplaces.our_places_api.model.Couple;
import br.com.ourplaces.our_places_api.model.Place;
import br.com.ourplaces.our_places_api.model.Rating;
import br.com.ourplaces.our_places_api.model.User;
import br.com.ourplaces.our_places_api.repository.CoupleRepository;
import br.com.ourplaces.our_places_api.repository.PlaceRepository;
import br.com.ourplaces.our_places_api.repository.RatingRepository;
import br.com.ourplaces.our_places_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final CoupleRepository coupleRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final PlaceMapper placeMapper;

    public PlaceService(PlaceRepository placeRepository, CoupleRepository coupleRepository, RatingRepository ratingRepository, UserRepository userRepository, PlaceMapper placeMapper) {
        this.placeRepository = placeRepository;
        this.coupleRepository = coupleRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
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

    @Transactional
    public List<PlaceViewDTO> findAllByCouple() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Couple couple = coupleRepository.findByUser1IdOrUser2Id(currentUser.getId(), currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Couple not found for the authenticated user."));

        List<Place> places = placeRepository.findAllByCoupleId(couple.getId());

        return places.stream().map(placeMapper::toPlaceViewDTO).collect(Collectors.toList());
    }

    @Transactional
    public PlaceViewDTO addRating(Long placeId, RatingDTO ratingDTO) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser = userRepository.findById(principal.getId()).orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found in database."));

        Place place = placeRepository.findById(placeId).orElseThrow(() -> new ResourceNotFoundException("Place not found with id: " + placeId));

        if (!place.getCouple().getUser1().getId().equals(currentUser.getId()) && !place.getCouple().getUser2().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("User is not allowed to rate this place.");
        }

        if (ratingRepository.existsByPlaceIdAndUserId(placeId, currentUser.getId())) {
            throw new UserAlreadyRatedException("User has already rated this place.");
        }

        Rating newRating = new Rating();
        newRating.setScore(ratingDTO.score());
        newRating.setComment(ratingDTO.comment());
        newRating.setUser(currentUser);
        newRating.setPlace(place);

        place.getRatings().add(newRating);
        Place updatedPlace = placeRepository.save(place);

        return placeMapper.toPlaceViewDTO(updatedPlace);
    }

}
