package com.artem.umbrella.dto;

import com.artem.umbrella.entity.Virus;
import com.artem.umbrella.enumeration.HealthStatus;

import java.util.Set;

public record HumanCreateDto(
        String name,
        HealthStatus healthStatus,

        Set<Virus> viruses,
        Long locationId) {
}
