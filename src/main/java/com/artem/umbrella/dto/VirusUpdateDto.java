package com.artem.umbrella.dto;

public record VirusUpdateDto(
        Long id,
        String name,
        int infectiousnessPercentage) {
}
