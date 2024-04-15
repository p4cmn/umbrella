package com.artem.umbrella.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record LocationCreateDto(

        @NotBlank(message = "Country name must not be blank")
        @Pattern(regexp = "^[A-Z][a-zA-Z]*$",
                message = "Name must start with an uppercase letter and contain only letters")
        String name) {
}
