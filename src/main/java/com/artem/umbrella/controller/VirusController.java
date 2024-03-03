package com.artem.umbrella.controller;

import com.artem.umbrella.dto.VirusCreateDto;
import com.artem.umbrella.dto.VirusUpdateDto;
import com.artem.umbrella.entity.Virus;
import com.artem.umbrella.servise.VirusService;
import lombok.RequiredArgsConstructor;
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
    public Virus create(@RequestBody VirusCreateDto virusCreateDto) {
        return virusService.create(virusCreateDto);
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
