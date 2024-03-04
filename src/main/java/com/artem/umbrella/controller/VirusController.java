package com.artem.umbrella.controller;

import com.artem.umbrella.converter.DtoConverter;
import com.artem.umbrella.dto.VirusCreateDto;
import com.artem.umbrella.dto.VirusDto;
import com.artem.umbrella.dto.VirusInfectDto;
import com.artem.umbrella.dto.VirusUpdateDto;
import com.artem.umbrella.entity.Virus;
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
