package com.artem.umbrella.controller;

import com.artem.umbrella.converter.DtoConverter;
import com.artem.umbrella.dto.LocationCreateDto;
import com.artem.umbrella.dto.LocationDto;
import com.artem.umbrella.dto.LocationUpdateDto;
import com.artem.umbrella.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/{id}")
    public LocationDto getById(@PathVariable final Long id) {
        var location = locationService.getById(id);
        return DtoConverter.toLocationDto(location);
    }

    @GetMapping
    public List<LocationDto> getAll() {
        var locations = locationService.getAll();
        return locations.stream().map(DtoConverter::toLocationDto).toList();
    }

    @PostMapping("/several")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LocationDto> createSeveral(@Valid @RequestBody final List<LocationCreateDto> locationCreateDtoList) {
        var locations = locationService.createSeveral(locationCreateDtoList);
        return locations.stream().map(DtoConverter::toLocationDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDto create(@Valid @RequestBody final LocationCreateDto locationCreateDto) {
        var location = locationService.create(locationCreateDto);
        return DtoConverter.toLocationDto(location);
    }

    @PutMapping
    public LocationDto update(@Valid @RequestBody final LocationUpdateDto locationUpdateDto) {
        var location = locationService.update(locationUpdateDto);
        return DtoConverter.toLocationDto(location);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable final Long id) {
        locationService.deleteById(id);
    }
}
