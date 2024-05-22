package com.artem.umbrella.converter;

import com.artem.umbrella.dto.*;
import com.artem.umbrella.entity.Human;
import com.artem.umbrella.entity.Location;
import com.artem.umbrella.entity.Virus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoConverter {

    public HumanDto toHumanDto(final Human human) {
        var humanVirusesDto = human.getViruses().stream()
                .map(DtoConverter::toHumanVirusDto).toList();
        return HumanDto.builder()
                .id(human.getId())
                .name(human.getName())
                .healthStatus(human.getHealthStatus())
                .viruses(humanVirusesDto)
                .location(toHumanLocationDto(human))
                .build();
    }

    private HumanVirusDto toHumanVirusDto(Virus virus) {
        return HumanVirusDto.builder()
                .id(virus.getId())
                .name(virus.getName())
                .infectiousnessPercentage(virus.getInfectiousnessPercentage())
                .build();
    }

    private HumanLocationDto toHumanLocationDto(final Human human) {
        return HumanLocationDto.builder()
                .id(human.getLocation().getId())
                .name(human.getLocation().getName())
                .build();
    }

    public LocationDto toLocationDto(final Location location) {
        var locationHumansDto = location.getHumans().stream()
                .map(DtoConverter::toLocationHumanDto).toList();
        return LocationDto.builder()
                .id(location.getId())
                .name(location.getName())
                .humans(locationHumansDto)
                .build();
    }

    public VirusDto toVirusDto(final Virus virus) {
        var virusHumansDto = virus.getHumans().stream()
                .map(DtoConverter::toVirusHumanDto).toList();
        return VirusDto.builder()
                .id(virus.getId())
                .name(virus.getName())
                .infectiousnessPercentage(virus.getInfectiousnessPercentage())
                .humans(virusHumansDto)
                .build();
    }

    public VirusLocationDto toVirusLocationDto(final Virus virus) {
        return VirusLocationDto.builder()
                .id(virus.getId())
                .name(virus.getName())
                .build();
    }

    private VirusHumanDto toVirusHumanDto(final Human human) {
        return VirusHumanDto.builder()
                .id(human.getId())
                .name(human.getName())
                .healthStatus(human.getHealthStatus())
                .location(toHumanLocationDto(human))
                .build();
    }

    private LocationHumanDto toLocationHumanDto(final Human human) {
        var humanVirusesDto = human.getViruses().stream()
                .map(DtoConverter::toHumanVirusDto).toList();
        return LocationHumanDto.builder()
                .id(human.getId())
                .name(human.getName())
                .healthStatus(human.getHealthStatus())
                .viruses(humanVirusesDto)
                .build();
    }
}
