package com.artem.umbrella.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LocationUpdateDto(

        @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "id must be a integer")
        Long id,

        @NotBlank(message = "Country name must not be blank")
        @Pattern(regexp = "^[A-Z][a-zA-Z]*$",
                message = "Name must start with an uppercase letter and contain only letters")
        String name) {
}
