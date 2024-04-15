package com.artem.umbrella.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record VirusUpdateDto(
        @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "id must be a integer")
        Long id,

        @NotBlank(message = "name must not be blank")
        String name,

        @Min(value = MIN_INFECTIOUSNESS_PERCENTAGE,
                message = "infectiousnessPercentage must be greater than or equal to " + MIN_INFECTIOUSNESS_PERCENTAGE)
        @Max(value = MAX_INFECTIOUSNESS_PERCENTAGE,
                message = "infectiousnessPercentage must be less than or equal to " + MAX_INFECTIOUSNESS_PERCENTAGE)
        @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "infectiousnessPercentage must be a integer")
        int infectiousnessPercentage) {
    public static final int MIN_INFECTIOUSNESS_PERCENTAGE = 0;
    public static final int MAX_INFECTIOUSNESS_PERCENTAGE = 100;
}
