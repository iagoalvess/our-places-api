package br.com.ourplaces.our_places_api.dto;

import br.com.ourplaces.our_places_api.model.NotificationInterval;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ImportantDateDTO(
        @NotBlank(message = "Description is required") String description,

        @NotNull(message = "Date is required") LocalDate date,

        @NotNull(message = "Notification preference is required") NotificationInterval notificationInterval) {

}
