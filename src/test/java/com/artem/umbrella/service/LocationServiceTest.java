package com.artem.umbrella.service;

import com.artem.umbrella.cache.CacheManager;
import com.artem.umbrella.dto.LocationCreateDto;
import com.artem.umbrella.dto.LocationUpdateDto;
import com.artem.umbrella.entity.Location;
import com.artem.umbrella.exception.EntityExistsException;
import com.artem.umbrella.exception.EntityNotFoundException;
import com.artem.umbrella.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private LocationService locationService;

    @Test
    void getById_LocationNotFound() {
        var id = 1L;
        Mockito.when(cacheManager.get(Location.class, id)).thenReturn(null);
        Mockito.when(locationRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> locationService.getById(id));
        Mockito.verify(cacheManager, Mockito.times(1)).get(Location.class, id);
        Mockito.verify(locationRepository, Mockito.times(1)).findById(id);
        Mockito.verify(cacheManager, Mockito.never()).put(Mockito.eq(Location.class), Mockito.eq(id), Mockito.any());
    }

    @Test
    void getById_LocationNotInCache() {
        var id = 1L;
        var location = new Location();
        Mockito.when(cacheManager.get(Location.class, id)).thenReturn(null);
        Mockito.when(locationRepository.findById(id)).thenReturn(Optional.of(location));
        Location result = locationService.getById(id);
        assertEquals(location, result);
        Mockito.verify(cacheManager, Mockito.times(1)).get(Location.class, id);
        Mockito.verify(locationRepository, Mockito.times(1)).findById(id);
        Mockito.verify(cacheManager, Mockito.times(1))
                .put(Mockito.eq(Location.class), Mockito.eq(id), Mockito.any(Location.class));
    }

    @Test
    void getById_LocationInCache() {
        var id = 1L;
        var location = new Location();
        Mockito.when(cacheManager.get(Location.class, id)).thenReturn(location);
        Location result = locationService.getById(id);
        assertEquals(location, result);
        Mockito.verify(cacheManager, Mockito.times(1)).get(Location.class, id);
        Mockito.verify(locationRepository, Mockito.never()).findById(id);
        Mockito.verify(cacheManager, Mockito.never())
                .put(Mockito.eq(Location.class), Mockito.eq(id), Mockito.any(Location.class));
    }

    @Test
    void getAll() {
        var locations = Collections.<Location>emptyList();
        Mockito.when(locationRepository.findAll()).thenReturn(locations);
        var result = locationService.getAll();
        assertEquals(locations, result);
        Mockito.verify(locationRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void create_LocationDoesNotExist() {
        var locationCreateDto = new LocationCreateDto("New Location");
        Mockito.when(locationRepository.existsByName(locationCreateDto.name())).thenReturn(false);
        var location = Location.builder().name(locationCreateDto.name()).humans(new ArrayList<>()).build();
        Mockito.when(locationRepository.saveAndFlush(Mockito.any(Location.class))).thenReturn(location);
        var result = locationService.create(locationCreateDto);
        assertEquals(location, result);
        Mockito.verify(locationRepository, Mockito.times(1))
                .existsByName(locationCreateDto.name());
        Mockito.verify(locationRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(Location.class));
        Mockito.verify(cacheManager, Mockito.times(1))
                .put(Mockito.eq(Location.class), Mockito.any(Long.class), Mockito.eq(location));
    }

    @Test
    public void create_LocationExists() {
        var locationCreateDto = new LocationCreateDto("Existing Location");
        Mockito.when(locationRepository.existsByName(locationCreateDto.name())).thenReturn(true);
        assertThrows(EntityExistsException.class, () -> {
            locationService.create(locationCreateDto);
        });
        Mockito.verify(locationRepository, Mockito.times(1))
                .existsByName(locationCreateDto.name());
        Mockito.verify(locationRepository, Mockito.never())
                .saveAndFlush(Mockito.any(Location.class));
        Mockito.verify(cacheManager, Mockito.never())
                .put(Mockito.eq(Location.class), Mockito.any(Long.class), Mockito.any(Location.class));
    }

    @Test
    public void createSeveral_LocationsDoNotExist() {
        var locationCreateDtoList = List.of(
                new LocationCreateDto("Location1"),
                new LocationCreateDto("Location2")
        );
        Mockito.when(locationRepository.existsByNameIn(List.of("Location1", "Location2"))).thenReturn(false);
        var locations = locationCreateDtoList.stream()
                .map(dto -> Location.builder().name(dto.name()).humans(new ArrayList<>()).build())
                .toList();
        Mockito.when(locationRepository.saveAllAndFlush(Mockito.anyList())).thenReturn(locations);
        var result = locationService.createSeveral(locationCreateDtoList);
        assertEquals(locations, result);
        Mockito.verify(locationRepository, Mockito.times(1))
                .existsByNameIn(List.of("Location1", "Location2"));
        Mockito.verify(locationRepository, Mockito.times(1)).saveAllAndFlush(Mockito.anyList());
        Mockito.verify(cacheManager, Mockito.times(1))
                .put(Mockito.eq(Location.class), Mockito.any(Long.class), Mockito.any(Location.class));
    }

    @Test
    public void createSeveral_LocationExists() {
        var locationCreateDtoList = List.of(
                new LocationCreateDto("Location1"),
                new LocationCreateDto("Location3")
        );
        Mockito.when(locationRepository.existsByNameIn(List.of("Location1", "Location2"))).thenReturn(true);
        assertThrows(EntityExistsException.class, () -> {
            locationService.createSeveral(locationCreateDtoList);
        });
        Mockito.verify(locationRepository, Mockito.times(1))
                .existsByNameIn(List.of("Location1", "Location2"));
        Mockito.verify(locationRepository, Mockito.never()).saveAllAndFlush(Mockito.anyList());
        Mockito.verify(cacheManager, Mockito.never())
                .put(Mockito.eq(Location.class), Mockito.any(Long.class), Mockito.any(Location.class));
    }

    @Test
    public void update_LocationExists() {
        var id = 1L;
        var locationUpdateDto = new LocationUpdateDto(id, "Updated Name");
        var existingLocation = new Location();
        Mockito.when(locationRepository.findById(id)).thenReturn(Optional.of(existingLocation));
        var result = locationService.update(locationUpdateDto);
        assertEquals("Updated Name", result.getName());
        Mockito.verify(locationRepository, Mockito.times(1)).findById(id);
        Mockito.verify(locationRepository, Mockito.times(1)).save(existingLocation);
        Mockito.verify(cacheManager, Mockito.times(1))
                .update(Mockito.eq(Location.class), Mockito.eq(id), Mockito.eq(existingLocation));
    }

    @Test
    public void update_LocationNotFound() {
        var id = 1L;
        var locationUpdateDto = new LocationUpdateDto(id, "Updated Name");
        Mockito.when(locationRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            locationService.update(locationUpdateDto);
        });
        Mockito.verify(locationRepository, Mockito.times(1)).findById(id);
        Mockito.verify(locationRepository, Mockito.never()).save(Mockito.any(Location.class));
        Mockito.verify(cacheManager, Mockito.never())
                .update(Mockito.any(Class.class), Mockito.any(Long.class), Mockito.any(Location.class));
    }

    @Test
    public void deleteById_LocationExists() {
        var id = 1L;
        Mockito.when(locationRepository.existsById(id)).thenReturn(true);
        locationService.deleteById(id);
        Mockito.verify(locationRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(cacheManager, Mockito.times(1)).remove(Location.class, id);
        Mockito.verify(locationRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    public void deleteById_LocationNotFound() {
        var id = 1L;
        Mockito.when(locationRepository.existsById(id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> {
            locationService.deleteById(id);
        });
        Mockito.verify(locationRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(cacheManager, Mockito.never()).remove(Mockito.any(Class.class), Mockito.any(Long.class));
        Mockito.verify(locationRepository, Mockito.never()).deleteById(id);
    }

}