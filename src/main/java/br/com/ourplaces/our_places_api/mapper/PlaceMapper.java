package br.com.ourplaces.our_places_api.mapper;

import br.com.ourplaces.our_places_api.dto.PlaceCreateDTO;
import br.com.ourplaces.our_places_api.dto.PlaceViewDTO;
import br.com.ourplaces.our_places_api.dto.RatingViewDTO;
import br.com.ourplaces.our_places_api.model.Place;
import br.com.ourplaces.our_places_api.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlaceMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "couple", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    Place toPlace(PlaceCreateDTO place);

    @Mapping(target = "username", source = "user.name")
    RatingViewDTO toRatingView(Rating rating);

    @Mapping(source = "ratings", target = "averageRating", qualifiedByName = "calculateAverageRating")
    PlaceViewDTO toPlaceViewDTO(Place place);

    @Named("calculateAverageRating")
    default double calculateAverageRating(List<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream().mapToInt(Rating::getScore).average().orElse(0.0);
    }
}
