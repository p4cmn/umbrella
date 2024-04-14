package com.artem.umbrella.service;

import com.artem.umbrella.dto.VirusCreateDto;
import com.artem.umbrella.dto.VirusInfectDto;
import com.artem.umbrella.dto.VirusUpdateDto;
import com.artem.umbrella.entity.Human;
import com.artem.umbrella.entity.Virus;
import com.artem.umbrella.enumeration.HealthStatus;
import com.artem.umbrella.exception.EntityExistsException;
import com.artem.umbrella.exception.EntityNotFoundException;
import com.artem.umbrella.exception.ImmunityException;
import com.artem.umbrella.repository.LocationRepository;
import com.artem.umbrella.repository.VirusRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
    public void getById_VirusExists() {
        var id = 1L;
        var virus = new Virus();
        Mockito.when(virusRepository.findById(id)).thenReturn(Optional.of(virus));
        var result = virusService.getById(id);
        assertEquals(virus, result);
        Mockito.verify(virusRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void getById_VirusNotFound() {
        var id = 1L;
        Mockito.when(virusRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            virusService.getById(id);
        });
        Mockito.verify(virusRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void getAll() {
        var viruses = new ArrayList<Virus>();
        Mockito.when(virusRepository.findAll()).thenReturn(viruses);
        var result = virusService.getAll();
        assertEquals(viruses, result);
        Mockito.verify(virusRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getAllByLocationId_LocationExists() {
        var locationId = 1L;
        var viruses = new ArrayList<Virus>();
        Mockito.when(virusRepository.findAllByLocationId(locationId)).thenReturn(viruses);
        var result = virusService.getAllByLocationId(locationId);
        assertEquals(viruses, result);
        Mockito.verify(locationService, Mockito.times(1)).getById(locationId);
        Mockito.verify(virusRepository, Mockito.times(1)).findAllByLocationId(locationId);
    }

    @Test
    public void getAllByLocationId_LocationNotFound() {
        var locationId = 1L;
        Mockito.when(locationService.getById(locationId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> {
            virusService.getAllByLocationId(locationId);
        });
        Mockito.verify(locationService, Mockito.times(1)).getById(locationId);
        Mockito.verify(virusRepository, Mockito.never()).findAllByLocationId(locationId);
    }

    @Test
    public void create_VirusDoesNotExist() {
        var virusCreateDto = new VirusCreateDto("New Virus", 70);
        Mockito.when(virusRepository.existsByName(virusCreateDto.name())).thenReturn(false);
        var virus = Virus.builder()
                .name(virusCreateDto.name())
                .infectiousnessPercentage(virusCreateDto.infectiousnessPercentage())
                .humans(new ArrayList<>())
                .build();
        Mockito.when(virusRepository.save(Mockito.any(Virus.class))).thenReturn(virus);
        var result = virusService.create(virusCreateDto);
        assertEquals(virus, result);
        Mockito.verify(virusRepository, Mockito.times(1)).existsByName(virusCreateDto.name());
        Mockito.verify(virusRepository, Mockito.times(1)).save(Mockito.any(Virus.class));
    }

    @Test
    public void create_VirusExists() {
        var virusCreateDto = new VirusCreateDto("Existing Virus", 50);
        Mockito.when(virusRepository.existsByName(virusCreateDto.name())).thenReturn(true);
        assertThrows(EntityExistsException.class, () -> {
            virusService.create(virusCreateDto);
        });
        Mockito.verify(virusRepository, Mockito.times(1)).existsByName(virusCreateDto.name());
        Mockito.verify(virusRepository, Mockito.never()).save(Mockito.any(Virus.class));
    }

    @Test
    public void createSeveral_VirusesDoNotExist() {
        var virusCreateDtoList = List.of(
                new VirusCreateDto("Virus1", 60),
                new VirusCreateDto("Virus2", 70)
        );
        Mockito.when(virusRepository.existsByName(Mockito.anyString())).thenReturn(false);
        var viruses = virusCreateDtoList.stream()
                .map(dto -> Virus.builder().name(dto.name())
                        .infectiousnessPercentage(dto.infectiousnessPercentage())
                        .humans(new ArrayList<>()).build())
                .toList();
        Mockito.when(virusRepository.saveAll(Mockito.anyList())).thenReturn(viruses);
        var result = virusService.createSeveral(virusCreateDtoList);
        assertEquals(viruses, result);
        Mockito.verify(virusRepository, Mockito.times(1)).existsByName("Virus1");
        Mockito.verify(virusRepository, Mockito.times(1)).existsByName("Virus2");
        Mockito.verify(virusRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

    @Test
    public void createSeveral_VirusesExist() {
        var virusCreateDtoList = List.of(
                new VirusCreateDto("Existing Virus", 50)
        );
        Mockito.when(virusRepository.existsByName(Mockito.anyString())).thenReturn(true);
        assertThrows(EntityExistsException.class, () -> {
            virusService.createSeveral(virusCreateDtoList);
        });
        Mockito.verify(virusRepository, Mockito.times(1)).existsByName("Existing Virus");
        Mockito.verify(virusRepository, Mockito.never()).saveAll(Mockito.anyList());
    }

    @Test
    public void infect_HumanAndVirusExist_Infected() {
        var humanId = 1L;
        var virusId = 1L;
        var human = new Human();
        var virus = Virus.builder().infectiousnessPercentage(60).build();
        var virusInfectDto = new VirusInfectDto(humanId, virusId);
        Mockito.when(humanService.getById(humanId)).thenReturn(human);
        Mockito.when(virusRepository.findById(virusId)).thenReturn(Optional.of(virus));
        virusService.infect(virusInfectDto);
        assertTrue(human.getViruses().contains(virus));
        assertEquals(HealthStatus.INFECTED, human.getHealthStatus());
        Mockito.verify(humanService, Mockito.times(1)).getById(humanId);
        Mockito.verify(virusRepository, Mockito.times(1)).findById(virusId);
        Mockito.verify(humanService, Mockito.times(1)).create(human);
    }

    @Test
    public void infect_HumanNotFound() {
        var humanId = 1L;
        var virusId = 1L;
        var virusInfectDto = new VirusInfectDto(humanId, virusId);
        Mockito.when(humanService.getById(humanId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> {
            virusService.infect(virusInfectDto);
        });
        Mockito.verify(humanService, Mockito.times(1)).getById(humanId);
        Mockito.verify(virusRepository, Mockito.never()).findById(Mockito.anyLong());
        Mockito.verify(humanService, Mockito.never()).create(Mockito.any(Human.class));
    }

    @Test
    public void infect_VirusNotFound() {
        var humanId = 1L;
        var virusId = 1L;
        var human = new Human();
        var virusInfectDto = new VirusInfectDto(humanId, virusId);
        Mockito.when(humanService.getById(humanId)).thenReturn(human);
        Mockito.when(virusRepository.findById(virusId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            virusService.infect(virusInfectDto);
        });
        Mockito.verify(humanService, Mockito.times(1)).getById(humanId);
        Mockito.verify(virusRepository, Mockito.times(1)).findById(virusId);
        Mockito.verify(humanService, Mockito.never()).create(Mockito.any(Human.class));
    }

    @Test
    public void infect_ImmunityException() {
        var humanId = 1L;
        var virusId = 1L;
        var human = new Human();
        var virus = Virus.builder().infectiousnessPercentage(40).build();
        var virusInfectDto = new VirusInfectDto(humanId, virusId);
        Mockito.when(humanService.getById(humanId)).thenReturn(human);
        Mockito.when(virusRepository.findById(virusId)).thenReturn(Optional.of(virus));
        assertThrows(ImmunityException.class, () -> {
            virusService.infect(virusInfectDto);
        });
        Mockito.verify(humanService, Mockito.times(1)).getById(humanId);
        Mockito.verify(virusRepository, Mockito.times(1)).findById(virusId);
        Mockito.verify(humanService, Mockito.never()).create(Mockito.any(Human.class));
    }

    @Test
    public void update_VirusExists() {
        var id = 1L;
        var newName = "Updated Name";
        var newInfectiousness = 70;
        var virusUpdateDto = new VirusUpdateDto(id, newName, newInfectiousness);
        var virus = Virus.builder().id(id).name("Old Name").infectiousnessPercentage(50).build();
        Mockito.when(virusRepository.findById(id)).thenReturn(java.util.Optional.of(virus));
        Mockito.when(virusRepository.save(Mockito.any(Virus.class))).thenAnswer(invocation -> invocation.getArgument(0));
        var result = virusService.update(virusUpdateDto);
        assertEquals(newName, result.getName());
        assertEquals(newInfectiousness, result.getInfectiousnessPercentage());
        Mockito.verify(virusRepository, Mockito.times(1)).findById(id);
        Mockito.verify(virusRepository, Mockito.times(1)).save(virus);
    }

    @Test
    public void update_VirusNotFound() {
        var id = 1L;
        var virusUpdateDto = new VirusUpdateDto(id, "Updated Name", 70);
        Mockito.when(virusRepository.findById(id)).thenReturn(java.util.Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            virusService.update(virusUpdateDto);
        });
        Mockito.verify(virusRepository, Mockito.times(1)).findById(id);
        Mockito.verify(virusRepository, Mockito.never()).save(Mockito.any(Virus.class));
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
        Mockito.when(virusRepository.findById(id)).thenReturn(java.util.Optional.of(virus));
        Mockito.doNothing().when(humanService).createAll(virus.getHumans());
        virusService.deleteById(id);
        assertTrue(human1.getViruses().isEmpty());
        assertTrue(human2.getViruses().isEmpty());
        Mockito.verify(virusRepository, Mockito.times(1)).deleteById(id);
        Mockito.verify(humanService, Mockito.times(1)).createAll(virus.getHumans());
    }

    @Test
    public void deleteById_VirusNotFound() {
        var id = 1L;
        Mockito.when(virusRepository.findById(id)).thenReturn(java.util.Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            virusService.deleteById(id);
        });
        Mockito.verify(virusRepository, Mockito.never()).deleteById(id);
        Mockito.verify(humanService, Mockito.never()).createAll(Mockito.any());
    }
}
