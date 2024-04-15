package com.artem.umbrella.service;

import com.artem.umbrella.dto.LocationCreateDto;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void infect_HumanAndVirusExist_Infected() {
        var humanId = 1L;
        var virusId = 1L;
        var human = new Human();
        var virus = Virus.builder().infectiousnessPercentage(60).build();
        var virusInfectDto = new VirusInfectDto(humanId, virusId);
        when(humanService.getById(humanId)).thenReturn(human);
        when(virusRepository.findById(virusId)).thenReturn(Optional.of(virus));
        virusService.infect(virusInfectDto);
        assertTrue(human.getViruses().contains(virus));
        assertEquals(HealthStatus.INFECTED, human.getHealthStatus());
        verify(humanService, Mockito.times(1)).getById(humanId);
        verify(virusRepository, Mockito.times(1)).findById(virusId);
        verify(humanService, Mockito.times(1)).create(human);
    }

    @Test
    public void infect_HumanNotFound() {
        var humanId = 1L;
        var virusId = 1L;
        var virusInfectDto = new VirusInfectDto(humanId, virusId);
        when(humanService.getById(humanId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> {
            virusService.infect(virusInfectDto);
        });
        verify(humanService, Mockito.times(1)).getById(humanId);
        verify(virusRepository, Mockito.never()).findById(Mockito.anyLong());
        verify(humanService, Mockito.never()).create(Mockito.any(Human.class));
    }

    @Test
    public void infect_VirusNotFound() {
        var humanId = 1L;
        var virusId = 1L;
        var human = new Human();
        var virusInfectDto = new VirusInfectDto(humanId, virusId);
        when(humanService.getById(humanId)).thenReturn(human);
        when(virusRepository.findById(virusId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            virusService.infect(virusInfectDto);
        });
        verify(humanService, Mockito.times(1)).getById(humanId);
        verify(virusRepository, Mockito.times(1)).findById(virusId);
        verify(humanService, Mockito.never()).create(Mockito.any(Human.class));
    }

    @Test
    public void infect_ImmunityException() {
        var humanId = 1L;
        var virusId = 1L;
        var human = new Human();
        var virus = Virus.builder().infectiousnessPercentage(40).build();
        var virusInfectDto = new VirusInfectDto(humanId, virusId);
        when(humanService.getById(humanId)).thenReturn(human);
        when(virusRepository.findById(virusId)).thenReturn(Optional.of(virus));
        assertThrows(ImmunityException.class, () -> {
            virusService.infect(virusInfectDto);
        });
        verify(humanService, Mockito.times(1)).getById(humanId);
        verify(virusRepository, Mockito.times(1)).findById(virusId);
        verify(humanService, Mockito.never()).create(Mockito.any(Human.class));
    }

    @Test
    public void update_VirusExists() {
        var id = 1L;
        var newName = "Updated Name";
        var newInfectiousness = 70;
        var virusUpdateDto = new VirusUpdateDto(id, newName, newInfectiousness);
        var virus = Virus.builder().id(id).name("Old Name").infectiousnessPercentage(50).build();
        when(virusRepository.findById(id)).thenReturn(java.util.Optional.of(virus));
        when(virusRepository.save(Mockito.any(Virus.class))).thenAnswer(invocation -> invocation.getArgument(0));
        var result = virusService.update(virusUpdateDto);
        assertEquals(newName, result.getName());
        assertEquals(newInfectiousness, result.getInfectiousnessPercentage());
        verify(virusRepository, Mockito.times(1)).findById(id);
        verify(virusRepository, Mockito.times(1)).save(virus);
    }

    @Test
    public void update_VirusNotFound() {
        var id = 1L;
        var virusUpdateDto = new VirusUpdateDto(id, "Updated Name", 70);
        when(virusRepository.findById(id)).thenReturn(java.util.Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            virusService.update(virusUpdateDto);
        });
        verify(virusRepository, Mockito.times(1)).findById(id);
        verify(virusRepository, Mockito.never()).save(Mockito.any(Virus.class));
    }

    @Test
    public void deleteById_VirusExists() {
        var id = 1L;
        var virus = new Virus();
        virus.setId(id);
        var human1 = new Human();
        human1.getViruses().add(virus);
        var human2 = new Human();
        human2.getViruses().add(virus);
        virus.getHumans().add(human1);
        virus.getHumans().add(human2);
        when(virusRepository.findById(id)).thenReturn(java.util.Optional.of(virus));
        Mockito.doNothing().when(humanService).createAll(virus.getHumans());
        virusService.deleteById(id);
        assertTrue(human1.getViruses().isEmpty());
        assertTrue(human2.getViruses().isEmpty());
        verify(virusRepository, Mockito.times(1)).deleteById(id);
        verify(humanService, Mockito.times(1)).createAll(virus.getHumans());
    }

    @Test
    public void deleteById_VirusNotFound() {
        var id = 1L;
        when(virusRepository.findById(id)).thenReturn(java.util.Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            virusService.deleteById(id);
        });
        verify(virusRepository, Mockito.never()).deleteById(id);
        verify(humanService, Mockito.never()).createAll(Mockito.any());
    }
}
