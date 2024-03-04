package com.artem.umbrella.dto;

import com.artem.umbrella.enumeration.HealthStatus;
import lombok.Builder;

@Builder
public record VirusHumanDto(
        Long id,
        String name,
        HealthStatus healthStatus,
        String location) {
}
