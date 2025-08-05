package br.com.ourplaces.our_places_api.dto;

import java.util.List;

public record PlaceViewDTO(Long id, String name, String address,
                           String category, double averageRating,
                           List<RatingViewDTO> ratings) {
}
