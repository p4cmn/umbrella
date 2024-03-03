package com.artem.umbrella.controller;

import com.artem.umbrella.dto.VirusCreateDto;
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
    public Virus getById(@PathVariable Long id) {
        return virusService.getById(id);
    }

    @GetMapping
    public List<Virus> getAll() {
        return virusService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Virus create(@RequestBody VirusCreateDto virusCreateDto) {
        return virusService.create(virusCreateDto);
    }

    @PostMapping("/infect")
    public void infect(@RequestBody VirusInfectDto virusInfectDto) {
        virusService.infect(virusInfectDto);
    }

    @PutMapping
    public Virus update(@RequestBody VirusUpdateDto virusUpdateDto) {
        return virusService.update(virusUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        virusService.deleteById(id);
    }
}
