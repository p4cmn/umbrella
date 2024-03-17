package com.artem.umbrella.controller;

import com.artem.umbrella.converter.DtoConverter;
import com.artem.umbrella.dto.*;
import com.artem.umbrella.servise.VirusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/viruses")
public class VirusController {

    private final VirusService virusService;

    @GetMapping("/{id}")
    public VirusDto getById(@PathVariable Long id) {
        var virus = virusService.getById(id);
        return DtoConverter.toVirusDto(virus);
    }

    @GetMapping
    public List<VirusDto> getAll() {
        var viruses = virusService.getAll();
        return viruses.stream().map(DtoConverter::toVirusDto).toList();
    }

    @GetMapping("/location/{locationId}")
    public List<VirusLocationDto> getAllByLocationId(@PathVariable Long locationId) {
        var viruses = virusService.getAllByLocationId(locationId);
        return viruses.stream().map(DtoConverter::toVirusLocationDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VirusDto create(@RequestBody VirusCreateDto virusCreateDto) {
        var virus = virusService.create(virusCreateDto);
        return DtoConverter.toVirusDto(virus);
    }

    @PostMapping("/infect")
    public void infect(@RequestBody VirusInfectDto virusInfectDto) {
        virusService.infect(virusInfectDto);
    }

    @PutMapping
    public VirusDto update(@RequestBody VirusUpdateDto virusUpdateDto) {
        var virus = virusService.update(virusUpdateDto);
        return DtoConverter.toVirusDto(virus);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        virusService.deleteById(id);
    }
}
