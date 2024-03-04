package com.artem.umbrella.dto;

import com.artem.umbrella.enumeration.HealthStatus;

public record HumanUpdateDto(
        Long id,
        String name,
        HealthStatus healthStatus,
        Long locationId) {
}
