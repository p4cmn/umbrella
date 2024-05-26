package com.artem.umbrella.controller;

import com.artem.umbrella.converter.DtoConverter;
import com.artem.umbrella.dto.*;
import com.artem.umbrella.service.VirusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/viruses")
public class VirusController {

    private final VirusService virusService;

    @GetMapping("/{id}")
    public VirusDto getById(@PathVariable final Long id) {
        var virus = virusService.getById(id);
        return DtoConverter.toVirusDto(virus);
    }

    @GetMapping
    public List<VirusDto> getAll() {
        var viruses = virusService.getAll();
        return viruses.stream().map(DtoConverter::toVirusDto).toList();
    }

    @GetMapping("/location/{locationId}")
    public List<VirusLocationDto> getAllByLocationId(@PathVariable final Long locationId) {
        var viruses = virusService.getAllByLocationId(locationId);
        return viruses.stream().map(DtoConverter::toVirusLocationDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VirusDto create(@Valid @RequestBody final VirusCreateDto virusCreateDto) {
        var virus = virusService.create(virusCreateDto);
        return DtoConverter.toVirusDto(virus);
    }

    @PostMapping("/several")
    @ResponseStatus(HttpStatus.CREATED)
    public List<VirusDto> createSeveral(@Valid @RequestBody final List<VirusCreateDto> virusCreateDtoList) {
        var viruses = virusService.createSeveral(virusCreateDtoList);
        return viruses.stream().map(DtoConverter::toVirusDto).toList();
    }

    @PostMapping("/infect")
    public void infect(@Valid @RequestBody final VirusInfectDto virusInfectDto) {
        virusService.infect(virusInfectDto);
    }

    @PutMapping
    public VirusDto update(@Valid @RequestBody final VirusUpdateDto virusUpdateDto) {
        var virus = virusService.update(virusUpdateDto);
        return DtoConverter.toVirusDto(virus);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable final Long id) {
        virusService.deleteById(id);
    }
}
