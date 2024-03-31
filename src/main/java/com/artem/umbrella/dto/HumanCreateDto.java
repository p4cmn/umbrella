package com.artem.umbrella.dto;

import com.artem.umbrella.enumeration.HealthStatus;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record HumanCreateDto(

        @NotNull(message = "Name must not be null")
        @NotBlank(message = "Name must not be blank")
        @Pattern(regexp = "^[A-Z][a-zA-Z]*$",
                message = "Name must start with an uppercase letter and contain only letters")
        String name,

        HealthStatus healthStatus,

        @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "id must be a integer")
        Long locationId) {
}
