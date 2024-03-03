package com.artem.umbrella.controller;

import com.artem.umbrella.dto.HumanCreateDto;
import com.artem.umbrella.dto.HumanUpdateDto;
import com.artem.umbrella.entity.Human;
import com.artem.umbrella.servise.HumanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/humans")
@RequiredArgsConstructor
public class HumanController {

    private final HumanService humanService;

    @GetMapping("/{id}")
    public Human getById(@PathVariable Long id) {
        return humanService.getById(id);
    }

    @GetMapping
    public List<Human> getAll() {
        return humanService.getAll();
    }

    @PostMapping
    public Human create(@RequestBody HumanCreateDto humanCreateDto) {
        return humanService.create(humanCreateDto);
    }

    @PutMapping
    public Human update(@RequestBody HumanUpdateDto humanUpdateDto) {
        return humanService.update(humanUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        humanService.deleteById(id);
    }
}
