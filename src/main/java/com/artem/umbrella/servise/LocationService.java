package com.artem.umbrella.servise;

import com.artem.umbrella.dto.LocationCreateDto;
import com.artem.umbrella.dto.LocationUpdateDto;
import com.artem.umbrella.dto.VirusCreateDto;
import com.artem.umbrella.dto.VirusUpdateDto;
import com.artem.umbrella.entity.Human;
import com.artem.umbrella.entity.Location;
import com.artem.umbrella.entity.Virus;
import com.artem.umbrella.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location getById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Location create(LocationCreateDto locationCreateDto) {
        if (locationRepository.existsByName(locationCreateDto.name())) {
            throw new RuntimeException();
        }
        var location = Location.builder()
                .name(locationCreateDto.name())
                .humans(new HashSet<>())
                .build();
        return locationRepository.save(location);
    }

    public Location update(LocationUpdateDto locationUpdateDto) {
        var location = getById(locationUpdateDto.id());
        location.setName(locationUpdateDto.name());
        return locationRepository.save(location);
    }

    public void deleteById(Long id) {
        getById(id);
        locationRepository.deleteById(id);
    }

}
