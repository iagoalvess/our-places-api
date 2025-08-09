package br.com.ourplaces.our_places_api.mapper;

import br.com.ourplaces.our_places_api.dto.PlaceCreateDTO;
import br.com.ourplaces.our_places_api.dto.PlaceViewDTO;
import br.com.ourplaces.our_places_api.dto.RatingViewDTO;
import br.com.ourplaces.our_places_api.model.Place;
import br.com.ourplaces.our_places_api.model.Rating;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlaceMapper {

    public Place toPlace(PlaceCreateDTO dto) {
        if (dto == null) return null;
        Place place = new Place();
        place.setName(dto.name());
        place.setAddress(dto.address());
        place.setCategory(dto.category());
        return place;
        }

    public RatingViewDTO toRatingView(Rating rating) {
        if (rating == null) return null;
        String username = rating.getUser() != null ? rating.getUser().getName() : null;
        return new RatingViewDTO(rating.getScore(), rating.getComment(), username);
    }

    public PlaceViewDTO toPlaceViewDTO(Place place) {
        if (place == null) return null;
        double average = calculateAverageRating(place.getRatings());
        List<RatingViewDTO> ratingViews = place.getRatings() == null ? List.of()
                : place.getRatings().stream().map(this::toRatingView).collect(Collectors.toList());
        return new PlaceViewDTO(place.getId(), place.getName(), place.getAddress(), place.getCategory(), average, ratingViews);
    }

    private double calculateAverageRating(List<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) return 0.0;
        return ratings.stream().mapToInt(Rating::getScore).average().orElse(0.0);
    }
}
