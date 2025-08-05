package br.com.ourplaces.our_places_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank(message = "Name is required") String name,

        @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,

        @NotBlank @Size(min = 6, message = "Password must be at least 6 characters long") String password) {
}

