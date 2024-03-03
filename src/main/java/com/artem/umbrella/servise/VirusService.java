package com.artem.umbrella.servise;

import com.artem.umbrella.dto.VirusCreateDto;
import com.artem.umbrella.dto.VirusUpdateDto;
import com.artem.umbrella.entity.Virus;
import com.artem.umbrella.repository.VirusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VirusService {

    private final VirusRepository virusRepository;

    public Virus getById(Long id) {
        return virusRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public List<Virus> getAll() {
        return virusRepository.findAll();
    }

    public Virus create(VirusCreateDto virusCreateDto) {
        if (virusRepository.existsByName(virusCreateDto.name())) {
            throw new RuntimeException();
        }
        var virus = Virus.builder()
                .name(virusCreateDto.name())
                .infectiousnessPercentage(virusCreateDto.infectiousnessPercentage())
                .build();
        return virusRepository.save(virus);
    }

    public Virus update(VirusUpdateDto virusUpdateDto) {
        var virus = getById(virusUpdateDto.id());
        virus.setName(virusUpdateDto.name());
        virus.setInfectiousnessPercentage(virusUpdateDto.infectiousnessPercentage());
        return virusRepository.save(virus);
    }

    public void deleteById(Long id) {
        getById(id);
        virusRepository.deleteById(id);
    }
}
