package com.artem.umbrella.servise;

import com.artem.umbrella.dto.VirusCreateDto;
import com.artem.umbrella.dto.VirusInfectDto;
import com.artem.umbrella.dto.VirusUpdateDto;
import com.artem.umbrella.entity.Virus;
import com.artem.umbrella.enumeration.HealthStatus;
import com.artem.umbrella.exception.EntityExistsException;
import com.artem.umbrella.exception.EntityNotFoundException;
import com.artem.umbrella.exception.ImmunityException;
import com.artem.umbrella.repository.VirusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VirusService {

    private final VirusRepository virusRepository;
    private final HumanService humanService;

    public Virus getById(Long id) {
        return virusRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Virus> getAll() {
        return virusRepository.findAll();
    }

    public Virus create(VirusCreateDto virusCreateDto) {
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

    public void infect(VirusInfectDto virusInfectDto) {
        var human = humanService.getById(virusInfectDto.humanId());
        var virus = getById(virusInfectDto.virusId());

        var infected = virus.getInfectiousnessPercentage() > 50;

        if (!infected) {
            throw new ImmunityException();
        }
        human.getViruses().add(virus);
        human.setHealthStatus(HealthStatus.INFECTED);
        humanService.create(human);
    }

    public Virus update(VirusUpdateDto virusUpdateDto) {
        var virus = getById(virusUpdateDto.id());
        virus.setName(virusUpdateDto.name());
        virus.setInfectiousnessPercentage(virusUpdateDto.infectiousnessPercentage());
        return virusRepository.save(virus);
    }

    @Transactional
    public void deleteById(Long id) {
        var virus = getById(id);
        virus.getHumans().forEach(human -> human.getViruses().remove(virus));
        humanService.createAll(virus.getHumans());
        virusRepository.deleteById(id);
    }
}
