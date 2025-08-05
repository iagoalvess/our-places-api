package br.com.ourplaces.our_places_api.dto;

import java.time.LocalDate;

public record ImportantDateDTO(String description,
                               LocalDate date) {
}
