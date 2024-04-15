package com.artem.umbrella.service;

import com.artem.umbrella.dto.VirusCreateDto;
import com.artem.umbrella.dto.VirusInfectDto;
import com.artem.umbrella.dto.VirusUpdateDto;
import com.artem.umbrella.entity.Human;
import com.artem.umbrella.entity.Location;
import com.artem.umbrella.entity.Virus;
import com.artem.umbrella.enumeration.HealthStatus;
import com.artem.umbrella.exception.EntityExistsException;
import com.artem.umbrella.exception.EntityNotFoundException;
import com.artem.umbrella.exception.ImmunityException;
import com.artem.umbrella.repository.VirusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VirusServiceTest {

    @Mock
    private VirusRepository virusRepository;

    @Mock
    private HumanService humanService;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private VirusService virusService;

    @Test
    public void getById_shouldReturnEntity_whenEntityExists() {
        var virus = Virus.builder()
                .id(1L)
                .name("Virus")
                .infectiousnessPercentage(30)
                .humans(new ArrayList<>())
                .build();
        when(virusRepository.findById(virus.getId())).thenReturn(Optional.of(virus));

        assertEquals(virus, virusService.getById(virus.getId()));
    }

    @Test
    public void getById_shouldThrowEntityNotFoundException_whenEntityDoesNotExist() {
        var virus = Virus.builder()
                .id(1L)
                .name("Virus")
                .infectiousnessPercentage(30)
                .humans(new ArrayList<>())
                .build();
        when(virusRepository.findById(virus.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> virusService.getById(virus.getId()));
    }

    @Test
    public void getAll_shouldReturnEntities_whenEntitiesExist() {
        var virus1 = Virus.builder()
                .id(1L)
                .name("Virus")
                .infectiousnessPercentage(30)
                .humans(new ArrayList<>())
                .build();
        var virus2 = Virus.builder()
                .id(1L)
                .name("Virus")
                .infectiousnessPercentage(30)
                .humans(new ArrayList<>())
                .build();
        var virusList = Arrays.asList(virus1, virus2);
        when(virusRepository.findAll()).thenReturn(virusList);

        assertEquals(virusList, virusService.getAll());
    }

    @Test
    public void getAll_shouldReturnEmptyList_whenEntitiesDoNotExist() {
        when(virusRepository.findAll()).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), virusService.getAll());
    }

    @Test
    public void getAllByLocationId_shouldReturnAllVirusesInLocation_whenLocationExists() {
        var location = Location.builder()
                .id(1L)
                .name("Location")
                .humans(new ArrayList<>())
                .build();
        var virus1 = Virus.builder()
                .id(1L)
                .name("Virus1")
                .infectiousnessPercentage(30)
                .humans(new ArrayList<>())
                .build();
        var virus2 = Virus.builder()
                .id(1L)
                .name("Virus2")
                .infectiousnessPercentage(70)
                .humans(new ArrayList<>())
                .build();
        var viruses = Arrays.asList(virus1, virus2);
        when(virusRepository.findAllByLocationId(location.getId())).thenReturn(viruses);

        locationService.getById(location.getId());
        assertEquals(viruses, virusService.getAllByLocationId(location.getId()));
    }

    @Test
    public void getAllByLocationId_shouldThrowEntityNotFoundException_whenLocationNotFound() {
        var location = Location.builder()
                .id(1L)
                .name("Location")
                .humans(new ArrayList<>())
                .build();
        when(locationService.getById(location.getId())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> virusService.getAllByLocationId(location.getId()));
    }

    @Test
    public void create_shouldCreateVirus_whenEntityExists() {
        var virusCreateDto = VirusCreateDto.builder()
                .name("virusCreateDto")
                .infectiousnessPercentage(20)
                .build();
        var virus = Virus.builder()
                .name(virusCreateDto.name())
                .infectiousnessPercentage(virusCreateDto.infectiousnessPercentage())
                .humans(new ArrayList<>())
                .build();
        when(virusRepository.existsByName(virusCreateDto.name())).thenReturn(false);
        when(virusRepository.save(virus)).thenReturn(virus);

        assertEquals(virus, virusService.create(virusCreateDto));
    }

    @Test
    public void create_shouldThrowEntityNotFoundException_whenEntityDoesNotExist() {
        var virusCreateDto = VirusCreateDto.builder()
                .name("virusCreateDto")
                .infectiousnessPercentage(20)
                .build();
        when(virusRepository.existsByName(virusCreateDto.name())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> virusService.create(virusCreateDto));
    }

    @Test
    public void createSeveral_shouldCreateEntities_whenEntitiesDoNotExist() {
        var virusCreateDto1 = VirusCreateDto.builder()
                .name("virusCreateDto1")
                .infectiousnessPercentage(20)
                .build();
        var virusCreateDto2 = VirusCreateDto.builder()
                .name("virusCreateDto2")
                .infectiousnessPercentage(20)
                .build();
        var virus1 = Virus.builder()
                .name(virusCreateDto1.name())
                .infectiousnessPercentage(virusCreateDto1.infectiousnessPercentage())
                .humans(new ArrayList<>())
                .build();
        var virus2 = Virus.builder()
                .name(virusCreateDto2.name())
                .infectiousnessPercentage(virusCreateDto2.infectiousnessPercentage())
                .humans(new ArrayList<>())
                .build();
        var virusCreateDtoList = Arrays.asList(virusCreateDto1, virusCreateDto2);
        var virusList = Arrays.asList(virus1, virus2);
        when(virusRepository.existsByNameIn(virusCreateDtoList.stream()
                .map(VirusCreateDto::name).toList())).thenReturn(false);

        assertEquals(virusList, virusService.createSeveral(virusCreateDtoList));

        verify(virusRepository, Mockito.times(1)).saveAll(virusList);
    }

    @Test
    public void createSeveral_shouldThrowEntityExistsException_whenEntitiesExist() {
        var virusCreateDto1 = VirusCreateDto.builder()
                .name("virusCreateDto1")
                .infectiousnessPercentage(20)
                .build();
        var virusCreateDto2 = VirusCreateDto.builder()
                .name("virusCreateDto2")
                .infectiousnessPercentage(20)
                .build();
        var virusCreateDtoList = Arrays.asList(virusCreateDto1, virusCreateDto2);
        when(virusRepository.existsByNameIn(virusCreateDtoList.stream()
                .map(VirusCreateDto::name).toList())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> virusService.createSeveral(virusCreateDtoList));
    }

    @Test
    public void infect_shouldInfectHuman_whenHumanAndVirusExists() {
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
        var virus = Virus.builder()
                .id(1L)
                .name("Virus")
                .infectiousnessPercentage(70)
                .humans(new ArrayList<>())
                .build();
        var expectedHuman = Human.builder()
                .id(1L)
                .name("Name")
                .healthStatus(HealthStatus.INFECTED)
                .location(location)
                .viruses(Collections.singletonList(virus))
                .build();
        var virusInfectDto = VirusInfectDto.builder()
                .virusId(virus.getId())
                .humanId(human.getId())
                .build();
        when(humanService.getById(virusInfectDto.humanId())).thenReturn(human);
        when(virusRepository.findById(virusInfectDto.virusId())).thenReturn(Optional.of(virus));

        virusService.infect(virusInfectDto);

        verify(humanService, Mockito.times(1)).create(expectedHuman);
    }

    @Test
    public void infect_shouldThrowEntityNotFoundException_whenHumanNotFound() {
        var virusInfectDto = VirusInfectDto.builder()
                .virusId(1L)
                .humanId(1L)
                .build();
        when(humanService.getById(virusInfectDto.humanId())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> virusService.infect(virusInfectDto));
        verify(humanService, never()).create(Mockito.any(Human.class));
    }

    @Test
    public void infect_shouldThrowEntityNotFoundException_whenVirusNotFound() {
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
        var virusInfectDto = VirusInfectDto.builder()
                .virusId(1L)
                .humanId(human.getId())
                .build();
        when(humanService.getById(virusInfectDto.humanId())).thenReturn(human);
        when(virusRepository.findById(virusInfectDto.virusId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> virusService.infect(virusInfectDto));
        verify(humanService, never()).create(any(Human.class));
    }

    @Test
    public void infect_shouldNotInfectHuman_whenHumanAndVirusExists() {
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
        var virus = Virus.builder()
                .id(1L)
                .name("Virus")
                .infectiousnessPercentage(30)
                .humans(new ArrayList<>())
                .build();
        var virusInfectDto = VirusInfectDto.builder()
                .virusId(virus.getId())
                .humanId(human.getId())
                .build();
        when(humanService.getById(virusInfectDto.humanId())).thenReturn(human);
        when(virusRepository.findById(virusInfectDto.virusId())).thenReturn(Optional.of(virus));

        assertThrows(ImmunityException.class, () -> virusService.infect(virusInfectDto));
        verify(humanService, never()).create(any(Human.class));
    }

    @Test
    public void update_shouldUpdateEntity_whenEntityExists() {
        var virusUpdateDto = VirusUpdateDto.builder()
                .id(1L)
                .name("virusUpdateDto")
                .infectiousnessPercentage(70)
                .build();
        var virusNew = Virus.builder()
                .id(virusUpdateDto.id())
                .name(virusUpdateDto.name())
                .infectiousnessPercentage(virusUpdateDto.infectiousnessPercentage())
                .humans(new ArrayList<>())
                .build();
        var virusOld = Virus.builder()
                .id(virusUpdateDto.id())
                .name("name")
                .infectiousnessPercentage(40)
                .humans(new ArrayList<>())
                .build();
        when(virusRepository.findById(virusUpdateDto.id())).thenReturn(Optional.of(virusOld));
        when(virusRepository.save(virusNew)).thenReturn(virusNew);

        assertEquals(virusNew, virusService.update(virusUpdateDto));
    }

    @Test
    public void update_VirusNotFound() {
        var virusUpdateDto = VirusUpdateDto.builder()
                .id(1L)
                .name("virusUpdateDto")
                .infectiousnessPercentage(70)
                .build();
        when(virusRepository.findById(virusUpdateDto.id())).thenReturn(java.util.Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> virusService.update(virusUpdateDto));
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
        var virus = Virus.builder()
                .id(1L)
                .name("Virus")
                .infectiousnessPercentage(30)
                .humans(Collections.singletonList(human))
                .build();
        when(virusRepository.findById(virus.getId())).thenReturn(Optional.of(virus));

        virusService.deleteById(virus.getId());
        verify(humanService, times(1)).createAll(virus.getHumans());
        verify(virusRepository, times(1)).deleteById(virus.getId());
    }

    @Test
    public void deleteById_shouldThrowEntityNotFoundException_whenVirusNotFound() {
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
        var virus = Virus.builder()
                .id(1L)
                .name("Virus")
                .infectiousnessPercentage(30)
                .humans(Collections.singletonList(human))
                .build();
        when(virusRepository.findById(virus.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> virusService.deleteById(virus.getId()));
    }
}
