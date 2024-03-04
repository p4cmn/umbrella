package com.artem.umbrella.dto;

import com.artem.umbrella.entity.Virus;
import com.artem.umbrella.enumeration.HealthStatus;

import java.util.Set;

public record HumanUpdateDto(
        Long id,
        String name,
        HealthStatus healthStatus,
        Long locationId) {
}
