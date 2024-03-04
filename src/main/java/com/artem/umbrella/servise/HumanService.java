package com.artem.umbrella.servise;

import com.artem.umbrella.dto.HumanCreateDto;
import com.artem.umbrella.dto.HumanUpdateDto;
import com.artem.umbrella.entity.Human;
import com.artem.umbrella.repository.HumanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
                .viruses(new ArrayList<>())
                .build();
        return humanRepository.save(human);
    }

    public void create(Human human) {
        humanRepository.save(human);
    }

    public void createAll(List<Human> humans) {
        humanRepository.saveAll(humans);
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
