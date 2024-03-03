package com.artem.umbrella.dto;

public record VirusCreateDto(
        String name,
        int infectiousnessPercentage) {
}
