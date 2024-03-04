package com.artem.umbrella.converter;

import com.artem.umbrella.dto.*;
import com.artem.umbrella.entity.Human;
import com.artem.umbrella.entity.Location;
import com.artem.umbrella.entity.Virus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoConverter {

    public HumanDto toHumanDto(Human human) {
        var viruses = human.getViruses().stream().map(Virus::getName).toList();
        return HumanDto.builder()
                .id(human.getId())
                .name(human.getName())
                .healthStatus(human.getHealthStatus())
                .viruses(viruses)
                .location(human.getLocation().getName())
                .build();
    }

    public LocationDto toLocationDto(Location location) {
        var locationHumansDto = location.getHumans().stream()
                .map(DtoConverter::toLocationHumanDto).toList();
        return LocationDto.builder()
                .id(location.getId())
                .name(location.getName())
                .humans(locationHumansDto)
                .build();
    }

    public VirusDto toVirusDto(Virus virus) {
        var virusHumansDto = virus.getHumans().stream()
                .map(DtoConverter::toVirusHumanDto).toList();
        return VirusDto.builder()
                .id(virus.getId())
                .name(virus.getName())
                .infectiousnessPercentage(virus.getInfectiousnessPercentage())
                .humans(virusHumansDto)
                .build();
    }

    private VirusHumanDto toVirusHumanDto(Human human) {
        return VirusHumanDto.builder()
                .id(human.getId())
                .name(human.getName())
                .healthStatus(human.getHealthStatus())
                .location(human.getLocation().getName())
                .build();
    }

    private LocationHumanDto toLocationHumanDto(Human human) {
        var viruses = human.getViruses().stream()
                .map(Virus::getName).toList();
        return LocationHumanDto.builder()
                .id(human.getId())
                .name(human.getName())
                .healthStatus(human.getHealthStatus())
                .viruses(viruses)
                .build();
    }
}
