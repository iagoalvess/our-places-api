package br.com.ourplaces.our_places_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RatingDTO(
        @Min(value = 1, message = "Score must be at least 1") @Max(value = 5, message = "Score must be at most 5") int score,
        String comment) {
}
