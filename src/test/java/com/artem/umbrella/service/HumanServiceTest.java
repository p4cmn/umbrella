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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void getById_HumanExists() {
        var id = 1L;
        var human = new Human();
        Mockito.when(humanRepository.findById(id)).thenReturn(Optional.of(human));
        var result = humanService.getById(id);
        assertEquals(human, result);
        Mockito.verify(humanRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void getById_HumanNotFound() {
        var id = 1L;
        Mockito.when(humanRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            humanService.getById(id);
        });
        Mockito.verify(humanRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void getAll() {
        var humans = new ArrayList<Human>();
        Mockito.when(humanRepository.findAll()).thenReturn(humans);
        var result = humanService.getAll();
        assertEquals(humans, result);
        Mockito.verify(humanRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void create_HumanCreateDto() {
        var humanCreateDto = new HumanCreateDto("John", HealthStatus.HEALTHY, 1L);
        var location = new Location();
        Mockito.when(locationService.getById(humanCreateDto.locationId())).thenReturn(location);
        humanService.create(humanCreateDto);
        Mockito.verify(locationService, Mockito.times(1))
                .getById(humanCreateDto.locationId());
        Mockito.verify(humanRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(Human.class));
        Mockito.verify(cacheManager, Mockito.times(1))
                .remove(Location.class, humanCreateDto.locationId());
    }

    @Test
    public void create_Human() {
        Human human = new Human();
        human.setLocation(new Location());
        humanService.create(human);
        Mockito.verify(humanRepository, Mockito.times(1))
                .save(Mockito.any(Human.class));
        Mockito.verify(cacheManager, Mockito.times(1))
                .remove(Location.class, human.getLocation().getId());
    }

    @Test
    public void createAll() {
        var humans = new ArrayList<Human>();
        Human human1 = new Human();
        human1.setLocation(new Location());
        Human human2 = new Human();
        human2.setLocation(new Location());
        humans.add(human1);
        humans.add(human2);
        humanService.createAll(humans);
        Mockito.verify(humanRepository, Mockito.times(1)).saveAll(humans);
        Mockito.verify(cacheManager, Mockito.times(2))
                .remove(Mockito.eq(Location.class), Mockito.anyLong());
    }

    @Test
    public void createSeveral() {
        var humanCreateDtoList = List.of(
                new HumanCreateDto("John", HealthStatus.HEALTHY, 1L),
                new HumanCreateDto("Jane", HealthStatus.INFECTED, 2L)
        );
        var location1 = new Location();
        var location2 = new Location();
        Mockito.when(locationService.getById(1L)).thenReturn(location1);
        Mockito.when(locationService.getById(2L)).thenReturn(location2);
        humanService.createSeveral(humanCreateDtoList);
        Mockito.verify(locationService, Mockito.times(1)).getById(1L);
        Mockito.verify(locationService, Mockito.times(1)).getById(2L);
        Mockito.verify(humanRepository, Mockito.times(1)).saveAllAndFlush(Mockito.anyList());
        Mockito.verify(cacheManager, Mockito.times(1)).remove(Location.class, 1L);
        Mockito.verify(cacheManager, Mockito.times(1)).remove(Location.class, 2L);
    }

    @Test
    public void update() {
        var id = 1L;
        var locationId = 2L;
        var humanUpdateDto = new HumanUpdateDto(id, "John", HealthStatus.HEALTHY, locationId);
        var human = new Human();
        human.setId(id);
        var oldLocation = new Location();
        oldLocation.setId(locationId);
        human.setLocation(oldLocation);
        var newLocation = new Location();
        Mockito.when(humanRepository.findById(id)).thenReturn(java.util.Optional.of(human));
        Mockito.when(locationService.getById(locationId)).thenReturn(newLocation);
        humanService.update(humanUpdateDto);
        Mockito.verify(humanRepository, Mockito.times(1)).findById(id);
        Mockito.verify(locationService, Mockito.times(1)).getById(locationId);
        Mockito.verify(humanRepository, Mockito.times(1)).saveAndFlush(human);
        Mockito.verify(cacheManager, Mockito.times(1))
                .remove(Location.class, oldLocation.getId());
        Mockito.verify(cacheManager, Mockito.times(1))
                .remove(Location.class, newLocation.getId());
        assertEquals("John", human.getName());
        assertEquals(HealthStatus.HEALTHY, human.getHealthStatus());
        assertEquals(newLocation, human.getLocation());
    }

    @Test
    public void testDeleteById() {
        var id = 1L;
        var locationId = 2L;
        var human = new Human();
        human.setId(id);
        var location = new Location();
        location.setId(locationId);
        human.setLocation(location);
        Mockito.when(humanRepository.findById(id)).thenReturn(java.util.Optional.of(human));
        humanService.deleteById(id);
        Mockito.verify(humanRepository, Mockito.times(1)).findById(id);
        Mockito.verify(humanRepository, Mockito.times(1)).deleteById(id);
        Mockito.verify(cacheManager, Mockito.times(1)).remove(Location.class, locationId);
    }
}