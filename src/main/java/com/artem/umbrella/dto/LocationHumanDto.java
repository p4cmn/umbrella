package com.artem.umbrella.dto;

import com.artem.umbrella.enumeration.HealthStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record LocationHumanDto(
        Long id,
        String name,
        HealthStatus healthStatus,
        List<HumanVirusDto> viruses) {
}
