package com.artem.umbrella.service;

import com.artem.umbrella.cache.CacheManager;
import com.artem.umbrella.dto.HumanCreateDto;
import com.artem.umbrella.dto.HumanUpdateDto;
import com.artem.umbrella.entity.Human;
import com.artem.umbrella.entity.Location;
import com.artem.umbrella.enumeration.HealthStatus;
import com.artem.umbrella.exception.EntityNotFoundException;
import com.artem.umbrella.repository.HumanRepository;
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
class HumanServiceTest {

    @Mock
    private HumanRepository humanRepository;

    @Mock
    private LocationService locationService;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private HumanService humanService;

    @Test
    public void getById_shouldReturnEntity_whenEntityExists() {
        var location = Location.builder()
                .id(1L)
                .name("Location")
                .humans(new ArrayList<>())
                .build();
        var human = Human.builder()
                .id(1L)
                .name("Name")
                .healthStatus(HealthStatus.HEALTHY)
                .location(location)
                .viruses(new ArrayList<>())
                .build();
        when(humanRepository.findById(human.getId())).thenReturn(Optional.of(human));

        assertEquals(human, humanService.getById(human.getId()));
    }

    @Test
    public void getById_shouldThrowEntityNotFoundException_whenEntityDoesNotExist() {
        when(humanRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> humanService.getById(1L));
    }

    @Test
    public void getAll_shouldReturnEntities_whenEntitiesExist() {
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
        var human1 = Human.builder()
                .name("Name1")
                .healthStatus(HealthStatus.ASYMPTOMATIC)
                .location(location1)
                .viruses(new ArrayList<>())
                .build();
        var human2 = Human.builder()
                .name("Name2")
                .healthStatus(HealthStatus.HEALTHY)
                .location(location2)
                .viruses(new ArrayList<>())
                .build();
        var humanList = Arrays.asList(human1, human2);
        when(humanRepository.findAll()).thenReturn(humanList);

        assertEquals(humanList, humanService.getAll());
    }

    @Test
    public void getAll_shouldReturnEmptyList_whenEntitiesDoNotExist() {
        when(humanRepository.findAll()).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), humanService.getAll());
    }

    @Test
    public void create_shouldCreateEntity() {
        var location = Location.builder()
                .id(1L)
                .name("Location")
                .humans(new ArrayList<>())
                .build();
        var humanCreateDto = HumanCreateDto.builder()
                .name("Name")
                .healthStatus(HealthStatus.HEALTHY)
                .locationId(location.getId())
                .build();
        var human = Human.builder()
                .name(humanCreateDto.name())
                .healthStatus(humanCreateDto.healthStatus())
                .location(location)
                .viruses(new ArrayList<>())
                .build();
        when(locationService.getById(humanCreateDto.locationId())).thenReturn(location);

        assertEquals(human, humanService.create(humanCreateDto));
        verify(cacheManager, times(1)).remove(Location.class, humanCreateDto.locationId());
    }

    @Test
    public void create() {
        var location = Location.builder()
                .id(1L)
                .name("Location")
                .humans(new ArrayList<>())
                .build();
        var human = Human.builder()
                .name("Name")
                .healthStatus(HealthStatus.HEALTHY)
                .location(location)
                .viruses(new ArrayList<>())
                .build();

        humanService.create(human);

        verify(humanRepository, times(1)).save(human);
        verify(cacheManager, times(1)).remove(Location.class, location.getId());
    }

    @Test
    public void createAll_shouldCreateEntities() {
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
        var human1 = Human.builder()
                .name("Name1")
                .healthStatus(HealthStatus.HEALTHY)
                .location(location1)
                .viruses(new ArrayList<>())
                .build();
        var human2 = Human.builder()
                .name("Name2")
                .healthStatus(HealthStatus.ASYMPTOMATIC)
                .location(location2)
                .viruses(new ArrayList<>())
                .build();
        var humanList = Arrays.asList(human1, human2);

        humanService.createAll(humanList);

        verify(humanRepository, times(1)).saveAll(humanList);
        verify(cacheManager, times(1)).remove(Location.class, location1.getId());
        verify(cacheManager, times(1)).remove(Location.class, location2.getId());
    }

    @Test
    public void createSeveral_shouldCreateEntities() {
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
        var humanCreateDto1 = HumanCreateDto.builder()
                .name("Name1")
                .healthStatus(HealthStatus.HEALTHY)
                .locationId(location1.getId())
                .build();
        var humanCreateDto2 = HumanCreateDto.builder()
                .name("Name2")
                .healthStatus(HealthStatus.INFECTED)
                .locationId(location2.getId())
                .build();
        var human1 = Human.builder()
                .name(humanCreateDto1.name())
                .healthStatus(humanCreateDto1.healthStatus())
                .location(location1)
                .viruses(new ArrayList<>())
                .build();
        var human2 = Human.builder()
                .name(humanCreateDto2.name())
                .healthStatus(humanCreateDto2.healthStatus())
                .location(location2)
                .viruses(new ArrayList<>())
                .build();
        var humanCreateDtoList = Arrays.asList(humanCreateDto1, humanCreateDto2);
        var humanList = Arrays.asList(human1, human2);
        when(locationService.getById(humanCreateDto1.locationId())).thenReturn(location1);
        when(locationService.getById(humanCreateDto2.locationId())).thenReturn(location2);

        humanService.createSeveral(humanCreateDtoList);

        verify(humanRepository, times(1)).saveAllAndFlush(humanList);
        verify(cacheManager, times(1)).remove(Location.class, location1.getId());
        verify(cacheManager, times(1)).remove(Location.class, location2.getId());
    }

    @Test
    public void update_shouldUpdateEntity_whenEntityExists() {
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
        var humanUpdateDto = HumanUpdateDto.builder()
                .id(1L)
                .name("Name")
                .healthStatus(HealthStatus.HEALTHY)
                .locationId(location2.getId())
                .build();
        var humanOld = Human.builder()
                .id(humanUpdateDto.id())
                .name(humanUpdateDto.name())
                .healthStatus(HealthStatus.ASYMPTOMATIC)
                .location(location1)
                .viruses(new ArrayList<>())
                .build();
        var humanNew = Human.builder()
                .id(humanUpdateDto.id())
                .name(humanUpdateDto.name())
                .healthStatus(humanUpdateDto.healthStatus())
                .location(location2)
                .viruses(new ArrayList<>())
                .build();
        when(humanRepository.findById(humanUpdateDto.id())).thenReturn(Optional.of(humanOld));
        when(locationService.getById(location2.getId())).thenReturn(location2);

        assertEquals(humanNew, humanService.update(humanUpdateDto));
        verify(cacheManager, times(1)).remove(Location.class, location1.getId());
        verify(cacheManager, times(1)).remove(Location.class, humanNew.getLocation().getId());
    }

    @Test
    public void deleteById_shouldDeleteEntity_whenEntityExists() {
        var location = Location.builder()
                .id(1L)
                .name("Location")
                .humans(new ArrayList<>())
                .build();
        var human = Human.builder()
                .id(1L)
                .name("Name")
                .healthStatus(HealthStatus.HEALTHY)
                .location(location)
                .viruses(new ArrayList<>())
                .build();
        when(humanRepository.findById(human.getId())).thenReturn(Optional.of(human));

        humanService.deleteById(human.getId());

        verify(humanRepository, times(1)).deleteById(human.getId());
        verify(cacheManager, times(1)).remove(Location.class, location.getId());
    }
}