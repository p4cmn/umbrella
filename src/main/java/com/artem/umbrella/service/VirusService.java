package com.artem.umbrella.service;

import com.artem.umbrella.dto.VirusCreateDto;
import com.artem.umbrella.dto.VirusInfectDto;
import com.artem.umbrella.dto.VirusUpdateDto;
import com.artem.umbrella.entity.Virus;
import com.artem.umbrella.enumeration.HealthStatus;
import com.artem.umbrella.exception.EntityExistsException;
import com.artem.umbrella.exception.EntityNotFoundException;
import com.artem.umbrella.exception.ImmunityException;
import com.artem.umbrella.repository.LocationRepository;
import com.artem.umbrella.repository.VirusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VirusService {

    private final VirusRepository virusRepository;
    private final HumanService humanService;
    private final LocationService locationService;
    private static final int INFECTIOUSNESS_THRESHOLD = 50;
    private final LocationRepository locationRepository;

    public Virus getById(final Long id) {
        return virusRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Virus> getAll() {
        return virusRepository.findAll();
    }

    public List<Virus> getAllByLocationId(final Long locationId) {
        locationService.getById(locationId);
        return virusRepository.findAllByLocationId(locationId);
    }

    public Virus create(final VirusCreateDto virusCreateDto) {
        if (virusRepository.existsByName(virusCreateDto.name())) {
            throw new EntityExistsException();
        }
        var virus = Virus.builder()
                .name(virusCreateDto.name())
                .infectiousnessPercentage(virusCreateDto.infectiousnessPercentage())
                .humans(new ArrayList<>())
                .build();
        return virusRepository.save(virus);
    }

    public List<Virus> createSeveral(final List<VirusCreateDto> virusCreateDtoList) {
        validateVirusesDoNotExist(virusCreateDtoList);
        var viruses = virusCreateDtoList.stream()
                .map(virusCreateDto -> Virus.builder()
                        .name(virusCreateDto.name())
                        .infectiousnessPercentage(virusCreateDto.infectiousnessPercentage())
                        .humans(new ArrayList<>())
                        .build())
                .toList();
        virusRepository.saveAll(viruses);
        return viruses;
    }

    public void infect(final VirusInfectDto virusInfectDto) {
        var human = humanService.getById(virusInfectDto.humanId());
        var virus = getById(virusInfectDto.virusId());
        var infected = virus.getInfectiousnessPercentage() > INFECTIOUSNESS_THRESHOLD;
        if (!infected) {
            throw new ImmunityException();
        }
        human.getViruses().add(virus);
        human.setHealthStatus(HealthStatus.INFECTED);
        humanService.create(human);
    }

    public Virus update(final VirusUpdateDto virusUpdateDto) {
        var virus = getById(virusUpdateDto.id());
        virus.setName(virusUpdateDto.name());
        virus.setInfectiousnessPercentage(virusUpdateDto.infectiousnessPercentage());
        return virusRepository.save(virus);
    }

    @Transactional
    public void deleteById(final Long id) {
        var virus = getById(id);
        virus.getHumans().forEach(human -> human.getViruses().remove(virus));
        humanService.createAll(virus.getHumans());
        virusRepository.deleteById(id);
    }

    private void validateVirusesDoNotExist(List<VirusCreateDto> virusCreateDtoList) {
        if (virusRepository.existsByNameIn(virusCreateDtoList.stream()
                .map(VirusCreateDto::name)
                .toList())) {
            throw new EntityExistsException();
        }
    }
}
