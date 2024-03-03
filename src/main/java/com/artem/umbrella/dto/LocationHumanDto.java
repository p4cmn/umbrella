package com.artem.umbrella.dto;

import com.artem.umbrella.entity.Virus;
import com.artem.umbrella.enumeration.HealthStatus;
import lombok.Builder;

import java.util.Set;

@Builder
public record LocationHumanDto(Long id,
                               String name,

                               Set<Virus> viruses,
                               HealthStatus healthStatus) {
}
