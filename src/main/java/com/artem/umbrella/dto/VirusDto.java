package com.artem.umbrella.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record VirusDto(
        Long id,
        String name,
        int infectiousnessPercentage,
        List<VirusHumanDto> humans) {
}
