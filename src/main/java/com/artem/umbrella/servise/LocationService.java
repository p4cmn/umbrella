package com.artem.umbrella.servise;

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

    public Location getById(Long id) {
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
        var updatedLocation = locationRepository.save(location);
        cacheManager.remove(Location.class, updatedLocation.getId());
        cacheManager.put(Location.class, updatedLocation.getId(), updatedLocation);
        return updatedLocation;
    }

    public void deleteById(Long id) {
        getById(id);
        cacheManager.remove(Location.class, id);
        locationRepository.deleteById(id);
    }

}
