package com.artem.umbrella.servise;

import com.artem.umbrella.cache.CacheManager;
import com.artem.umbrella.dto.HumanCreateDto;
import com.artem.umbrella.dto.HumanUpdateDto;
import com.artem.umbrella.entity.Human;
import com.artem.umbrella.entity.Location;
import com.artem.umbrella.exception.EntityNotFoundException;
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
    private final CacheManager cacheManager;

    public Human getById(Long id) {
        return humanRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
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
        humanRepository.saveAndFlush(human);
        cacheManager.remove(Location.class, humanCreateDto.locationId());
        return human;
    }

    public void create(Human human) {
        humanRepository.save(human);
        cacheManager.remove(Location.class, human.getLocation().getId());
    }

    public void createAll(List<Human> humans) {
        humanRepository.saveAll(humans);
        humans.forEach(human -> cacheManager.remove(Location.class, human.getLocation().getId()));
    }

    public Human update(HumanUpdateDto humanUpdateDto) {
        var human = getById(humanUpdateDto.id());
        var locationId = human.getLocation().getId();
        var location = locationService.getById(humanUpdateDto.locationId());
        human.setName(humanUpdateDto.name());
        human.setHealthStatus(humanUpdateDto.healthStatus());
        human.setLocation(location);
        humanRepository.saveAndFlush(human);
        cacheManager.remove(Location.class, locationId);
        cacheManager.remove(Location.class, human.getLocation().getId());
        return human;
    }

    public void deleteById(Long id) {
        var human = getById(id);
        humanRepository.deleteById(id);
        cacheManager.remove(Location.class, human.getLocation().getId());
    }
}
