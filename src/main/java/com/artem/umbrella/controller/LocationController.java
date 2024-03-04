package com.artem.umbrella.controller;

import com.artem.umbrella.converter.DtoConverter;
import com.artem.umbrella.dto.LocationCreateDto;
import com.artem.umbrella.dto.LocationDto;
import com.artem.umbrella.dto.LocationUpdateDto;
import com.artem.umbrella.servise.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/{id}")
    public LocationDto getById(@PathVariable Long id) {
        var location = locationService.getById(id);
        return DtoConverter.toLocationDto(location);
    }

    @GetMapping
    public List<LocationDto> getAll() {
        var locations = locationService.getAll();
        return locations.stream().map(DtoConverter::toLocationDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDto create(@RequestBody LocationCreateDto locationCreateDto) {
        var location = locationService.create(locationCreateDto);
        return DtoConverter.toLocationDto(location);
    }

    @PutMapping
    public LocationDto update(@RequestBody LocationUpdateDto locationUpdateDto) {
        var location = locationService.update(locationUpdateDto);
        return DtoConverter.toLocationDto(location);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        locationService.deleteById(id);
    }
}
