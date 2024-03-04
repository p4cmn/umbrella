package com.artem.umbrella.servise;

import com.artem.umbrella.dto.LocationCreateDto;
import com.artem.umbrella.dto.LocationUpdateDto;
import com.artem.umbrella.entity.Location;
import com.artem.umbrella.exception.EntityExistsException;
import com.artem.umbrella.exception.EntityNotFoundException;
import com.artem.umbrella.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location getById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Location create(LocationCreateDto locationCreateDto) {
        if (locationRepository.existsByName(locationCreateDto.name())) {
            throw new EntityExistsException();
        }
        var location = Location.builder()
                .name(locationCreateDto.name())
                .humans(new ArrayList<>())
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
