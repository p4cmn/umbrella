package com.artem.umbrella.dto;

import lombok.Builder;

@Builder
public record HumanLocationDto(Long id,
                               String name) {
}
