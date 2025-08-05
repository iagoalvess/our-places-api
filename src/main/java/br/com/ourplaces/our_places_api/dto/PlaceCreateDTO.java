package br.com.ourplaces.our_places_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlaceCreateDTO(
        @NotBlank(message = "Name is required") String name,

        String address,

        @NotBlank(message = "Category is required") String category,

        @NotNull(message = "Your rating is required when creating a place") @Valid RatingDTO initialRating) {
}
