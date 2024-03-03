package com.artem.umbrella.controller;

import com.artem.umbrella.converter.DtoConverter;
import com.artem.umbrella.dto.HumanCreateDto;
import com.artem.umbrella.dto.HumanDto;
import com.artem.umbrella.dto.HumanUpdateDto;
import com.artem.umbrella.entity.Human;
import com.artem.umbrella.servise.HumanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/humans")
@RequiredArgsConstructor
public class HumanController {

    private final HumanService humanService;

    @GetMapping("/{id}")
    public HumanDto getById(@PathVariable Long id) {
        var human = humanService.getById(id);
        return DtoConverter.toHumanDto(human);
    }

    @GetMapping
    public List<HumanDto> getAll() {
        var humans = humanService.getAll();
        return humans.stream().map(DtoConverter::toHumanDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HumanDto create(@RequestBody HumanCreateDto humanCreateDto) {
        var human = humanService.create(humanCreateDto);
        return DtoConverter.toHumanDto(human);
    }

    @PutMapping
    public HumanDto update(@RequestBody HumanUpdateDto humanUpdateDto) {
        var human = humanService.update(humanUpdateDto);
        return DtoConverter.toHumanDto(human);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        humanService.deleteById(id);
    }
}
