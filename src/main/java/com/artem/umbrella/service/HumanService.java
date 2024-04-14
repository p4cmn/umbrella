package com.artem.umbrella.service;

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

    public Human getById(final Long id) {
        return humanRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Human> getAll() {
        return humanRepository.findAll();
    }

    public Human create(final HumanCreateDto humanCreateDto) {
        var human = Human.builder()
                .name(humanCreateDto.name())
                .healthStatus(humanCreateDto.healthStatus())
                .location(locationService.getById(humanCreateDto.locationId()))
                .viruses(new ArrayList<>())
                .build();
        humanRepository.saveAndFlush(human);
        cacheManager.remove(Location.class, humanCreateDto.locationId());
        return human;
    }

    public void create(final Human human) {
        humanRepository.save(human);
        cacheManager.remove(Location.class, human.getLocation().getId());
    }

    public void createAll(final List<Human> humans) {
        humanRepository.saveAll(humans);
        humans.forEach(human -> cacheManager.remove(Location.class, human.getLocation().getId()));
    }

    public List<Human> createSeveral(final List<HumanCreateDto> humanCreateDtoList) {
        var humans = humanCreateDtoList.stream()
                .map(humanCreateDto -> Human.builder()
                        .name(humanCreateDto.name())
                        .healthStatus(humanCreateDto.healthStatus())
                        .location(locationService.getById(humanCreateDto.locationId()))
                        .viruses(new ArrayList<>())
                        .build()).toList();
        humanRepository.saveAllAndFlush(humans);
        humans.forEach(human -> cacheManager.remove(Location.class, human.getLocation().getId()));
        return humans;
    }

    public Human update(final HumanUpdateDto humanUpdateDto) {
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

    public void deleteById(final Long id) {
        var human = getById(id);
        humanRepository.deleteById(id);
        cacheManager.remove(Location.class, human.getLocation().getId());
    }
}
