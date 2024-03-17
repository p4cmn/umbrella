package com.artem.umbrella.dto;

import lombok.Builder;

@Builder
public record VirusLocationDto(
        Long id,
        String name) {
}
