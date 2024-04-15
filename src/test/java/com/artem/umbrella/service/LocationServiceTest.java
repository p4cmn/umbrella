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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private LocationService locationService;

    @Test
    void getById_shouldThrowEntityNotFoundException_whenEntityNotFound() {
        when(cacheManager.get(Location.class, 1L)).thenReturn(null);
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> locationService.getById(1L));
    }

    @Test
    void getById_shouldReturnLocation_whenLocationNotInCache() {
        var location = Location.builder()
                .id(1L)
                .name("Location")
                .humans(new ArrayList<>())
                .build();
        when(cacheManager.get(Location.class, location.getId())).thenReturn(null);
        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));

        assertEquals(location, locationService.getById(location.getId()));

        verify(cacheManager, times(1)).put(Location.class, location.getId(), location);
    }

    @Test
    void getById_shouldReturnLocation_whenLocationInCache() {
        var location = Location.builder()
                .id(1L)
                .name("Location")
                .humans(new ArrayList<>())
                .build();
        when(cacheManager.get(Location.class, location.getId())).thenReturn(location);

        assertEquals(location, locationService.getById(location.getId()));
    }

    @Test
    void getAll_shouldReturnEntities_whenEntitiesExist() {
        var location1 = Location.builder()
                .id(1L)
                .name("Location1")
                .humans(new ArrayList<>())
                .build();
        var location2 = Location.builder()
                .id(2L)
                .name("Location2")
                .humans(new ArrayList<>())
                .build();
        var locationList = Arrays.asList(location1, location2);
        when(locationRepository.findAll()).thenReturn(locationList);

        assertEquals(locationList, locationService.getAll());
    }

    @Test
    public void getAll_shouldReturnEmptyList_whenEntitiesDoNotExist() {
        when(locationRepository.findAll()).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), locationService.getAll());
    }

    @Test
    public void create_shouldCreateEntity_whenEntitiesDoNotExist() {
        var locationCreateDto = LocationCreateDto.builder()
                .name("locationCreateDto")
                .build();
        var location = Location.builder()
                .name(locationCreateDto.name())
                .humans(new ArrayList<>())
                .build();
        when(locationRepository.existsByName(locationCreateDto.name())).thenReturn(false);

        assertEquals(location, locationService.create(locationCreateDto));

        verify(locationRepository, times(1)).saveAndFlush(location);
        verify(cacheManager, times(1)).put(Location.class, location.getId(), location);
    }

    @Test
    public void create_shouldThrowEntityExistsException_whenEntitiesExist() {
        var locationCreateDto = LocationCreateDto.builder()
                .name("locationCreateDto")
                .build();
        var location = Location.builder()
                .name(locationCreateDto.name())
                .humans(new ArrayList<>())
                .build();
        when(locationRepository.existsByName(locationCreateDto.name())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> locationService.create(locationCreateDto));
    }

    @Test
    public void createSeveral_shouldCreateEntities_whenEntitiesDoNotExist() {
        var locationCreateDto1 = LocationCreateDto.builder()
                .name("locationCreateDto1")
                .build();
        var locationCreateDto2 = LocationCreateDto.builder()
                .name("locationCreateDto2")
                .build();
        var location1 = Location.builder()
                .name(locationCreateDto1.name())
                .humans(new ArrayList<>())
                .build();
        var location2 = Location.builder()
                .name(locationCreateDto2.name())
                .humans(new ArrayList<>())
                .build();
        var locationCreateDtoList = Arrays.asList(locationCreateDto1, locationCreateDto2);
        var locationList = Arrays.asList(location1, location2);
        when(locationRepository.existsByNameIn(locationCreateDtoList.stream()
                .map(LocationCreateDto::name).toList())).thenReturn(false);

        assertEquals(locationList, locationService.createSeveral(locationCreateDtoList));

        verify(locationRepository, times(1)).saveAllAndFlush(locationList);
        verify(cacheManager, times(1)).put(Location.class, location1.getId(), location1);
        verify(cacheManager, times(1)).put(Location.class, location2.getId(), location2);
    }

    @Test
    public void createSeveral_shouldThrowEntityExistsException_whenEntitiesExist() {
        var locationCreateDto1 = LocationCreateDto.builder()
                .name("locationCreateDto1")
                .build();
        var locationCreateDto2 = LocationCreateDto.builder()
                .name("locationCreateDto2")
                .build();
        var locationCreateDtoList = Arrays.asList(locationCreateDto1, locationCreateDto2);
        when(locationRepository.existsByNameIn(locationCreateDtoList.stream()
                .map(LocationCreateDto::name).toList())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> locationService.createSeveral(locationCreateDtoList));
    }

    @Test
    public void update_shouldUpdateEntity_whenEntityExists() {
        var locationUpdateDto = LocationUpdateDto.builder()
                .id(1L)
                .name("locationUpdateDto")
                .build();
        var locationOld = Location.builder()
                .id(locationUpdateDto.id())
                .name("Location")
                .humans(new ArrayList<>())
                .build();
        var locationNew = Location.builder()
                .id(locationUpdateDto.id())
                .name(locationUpdateDto.name())
                .humans(new ArrayList<>())
                .build();
        when(locationRepository.findById(locationUpdateDto.id())).thenReturn(Optional.of(locationOld));
        when(locationRepository.save(locationNew)).thenReturn(locationNew);

        assertEquals(locationNew, locationService.update(locationUpdateDto));

        verify(cacheManager, times(1)).update(Location.class, locationNew.getId(), locationNew);
    }

    @Test
    public void update_shouldThrowEntityNotFoundException_whenEntityNotFound() {
        var locationUpdateDto = LocationUpdateDto.builder()
                .id(1L)
                .name("locationUpdateDto")
                .build();
        when(locationRepository.findById(locationUpdateDto.id())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> locationService.update(locationUpdateDto));
    }

    @Test
    public void deleteById_shouldDeleteEntity_whenEntityExists() {
        var location = Location.builder()
                .id(1L)
                .name("Location")
                .humans(new ArrayList<>())
                .build();
        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));

        locationService.deleteById(location.getId());

        verify(locationRepository, times(1)).deleteById(location.getId());
        verify(cacheManager, times(1)).remove(Location.class, location.getId());
    }

    @Test
    public void deleteById_shouldThrowEntityNotFoundException_whenEntityNotFound() {
        var location = Location.builder()
                .id(1L)
                .name("Location")
                .humans(new ArrayList<>())
                .build();
        when(locationRepository.findById(location.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> locationService.deleteById(location.getId()));
    }
}