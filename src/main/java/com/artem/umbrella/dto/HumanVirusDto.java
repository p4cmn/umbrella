package com.artem.umbrella.dto;

import lombok.Builder;

@Builder
public record HumanVirusDto(Long id,
                            String name,
                            int infectiousnessPercentage) {
}
