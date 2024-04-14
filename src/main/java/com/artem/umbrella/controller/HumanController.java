package com.artem.umbrella.controller;

import com.artem.umbrella.converter.DtoConverter;
import com.artem.umbrella.dto.HumanCreateDto;
import com.artem.umbrella.dto.HumanDto;
import com.artem.umbrella.dto.HumanUpdateDto;
import com.artem.umbrella.service.HumanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/humans")
@RequiredArgsConstructor
public class HumanController {

    private final HumanService humanService;

    @GetMapping("/{id}")
    public HumanDto getById(@PathVariable final Long id) {
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
    public HumanDto create(@Valid @RequestBody final HumanCreateDto humanCreateDto) {
        var human = humanService.create(humanCreateDto);
        return DtoConverter.toHumanDto(human);
    }

    @PostMapping("/several")
    @ResponseStatus(HttpStatus.CREATED)
    public List<HumanDto> createSeveral(@RequestBody final List<@Valid HumanCreateDto> humanCreateDtoList) {
        var humans = humanService.createSeveral(humanCreateDtoList);
        return humans.stream().map(DtoConverter::toHumanDto).toList();
    }

    @PutMapping
    public HumanDto update(@Valid @RequestBody final HumanUpdateDto humanUpdateDto) {
        var human = humanService.update(humanUpdateDto);
        return DtoConverter.toHumanDto(human);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable final Long id) {
        humanService.deleteById(id);
    }
}
