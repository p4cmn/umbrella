package com.artem.umbrella.service;

import com.artem.umbrella.cache.CacheManager;
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
    private final CacheManager cacheManager;

    public Location getById(final Long id) {
        var cachedLocation = cacheManager.get(Location.class, id);
        if (cachedLocation != null) {
            return (Location) cachedLocation;
        }
        var location = locationRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        cacheManager.put(Location.class, id, location);
        return location;
    }

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Location create(final LocationCreateDto locationCreateDto) {
        if (locationRepository.existsByName(locationCreateDto.name())) {
            throw new EntityExistsException();
        }
        var location = Location.builder()
                .name(locationCreateDto.name())
                .humans(new ArrayList<>())
                .build();
        locationRepository.saveAndFlush(location);
        cacheManager.put(Location.class, location.getId(), location);
        return location;
    }

    public List<Location> createSeveral(final List<LocationCreateDto> locationCreateDtoList) {
        validateLocationsDoNotExist(locationCreateDtoList);
        var locations = locationCreateDtoList.stream()
                .map(locationCreateDto -> Location.builder()
                        .name(locationCreateDto.name())
                        .humans(new ArrayList<>())
                        .build())
                .toList();
        locationRepository.saveAllAndFlush(locations);
        locations.forEach(location -> cacheManager.put(Location.class, location.getId(), location));
        return locations;
    }

    public Location update(final LocationUpdateDto locationUpdateDto) {
        var location = getById(locationUpdateDto.id());
        location.setName(locationUpdateDto.name());
        var updatedLocation = locationRepository.save(location);
        cacheManager.update(Location.class, location.getId(), location);
        return updatedLocation;
    }

    public void deleteById(final Long id) {
        getById(id);
        cacheManager.remove(Location.class, id);
        locationRepository.deleteById(id);
    }

    private void validateLocationsDoNotExist(List<LocationCreateDto> locationCreateDtoList) {
        if (locationRepository.existsByNameIn(locationCreateDtoList.stream()
                .map(LocationCreateDto::name)
                .toList())) {
            throw new EntityExistsException();
        }
    }
}
