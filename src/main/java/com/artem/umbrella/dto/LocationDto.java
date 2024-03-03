package com.artem.umbrella.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record LocationDto(Long id,
                          String name,
                          List<LocationHumanDto> humans) {
}
