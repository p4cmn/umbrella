package com.artem.umbrella.dto;

import com.artem.umbrella.enumeration.HealthStatus;

public record HumanCreateDto(
        String name,
        HealthStatus healthStatus,
        Long locationId) {
}
