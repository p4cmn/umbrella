package com.artem.umbrella.servise;

import com.artem.umbrella.dto.*;
import com.artem.umbrella.entity.Human;
import com.artem.umbrella.entity.Virus;
import com.artem.umbrella.repository.HumanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HumanService {

    private final HumanRepository humanRepository;
    private final LocationService locationService;

    public Human getById(Long id) {
        return humanRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public List<Human> getAll() {
        return humanRepository.findAll();
    }

    public Human create(HumanCreateDto humanCreateDto) {
        var location = locationService.getById(humanCreateDto.locationId());
        var human = Human.builder()
                .name(humanCreateDto.name())
                .healthStatus(humanCreateDto.healthStatus())
                .location(location)
                .build();
        return humanRepository.save(human);
    }

    public void create(Human human) {
        humanRepository.save(human);
    }

    public Human update(HumanUpdateDto humanUpdateDto) {
        var human = getById(humanUpdateDto.id());
        var location = locationService.getById(humanUpdateDto.locationId());
        human.setName(humanUpdateDto.name());
        human.setHealthStatus(humanUpdateDto.healthStatus());
        human.setLocation(location);
        return humanRepository.save(human);
    }

    public void deleteById(Long id) {
        getById(id);
        humanRepository.deleteById(id);
    }
}
