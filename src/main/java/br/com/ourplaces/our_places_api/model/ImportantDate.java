package br.com.ourplaces.our_places_api.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportantDate {
    private String description;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private NotificationInterval notificationInterval;
}
