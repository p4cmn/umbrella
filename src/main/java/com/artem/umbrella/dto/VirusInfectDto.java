package com.artem.umbrella.dto;

import lombok.Builder;

@Builder
public record VirusInfectDto(
        Long virusId,
        Long humanId) {
}
