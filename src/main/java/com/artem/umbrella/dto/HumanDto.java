package com.artem.umbrella.dto;

import com.artem.umbrella.entity.Virus;
import com.artem.umbrella.enumeration.HealthStatus;
import lombok.Builder;

import java.util.Set;

@Builder
public record HumanDto(Long id,
                       String name,
                       HealthStatus healthStatus,
                       Set<Virus> viruses,
                       String location) {
}
