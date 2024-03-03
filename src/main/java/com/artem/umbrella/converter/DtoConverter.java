package com.artem.umbrella.converter;

import com.artem.umbrella.dto.HumanDto;
import com.artem.umbrella.dto.LocationDto;
import com.artem.umbrella.dto.LocationHumanDto;
import com.artem.umbrella.entity.Human;
import com.artem.umbrella.entity.Location;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoConverter {


    public HumanDto toHumanDto(Human human) {
        return HumanDto.builder()
                .id(human.getId())
                .name(human.getName())
                .healthStatus(human.getHealthStatus())
                .viruses(human.getViruses())
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

    private LocationHumanDto toLocationHumanDto(Human human) {
        return LocationHumanDto.builder()
                .id(human.getId())
                .name(human.getName())
                .healthStatus(human.getHealthStatus())
                .viruses(human.getViruses())
                .build();
    }
}
