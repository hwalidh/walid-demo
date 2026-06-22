package com.walid.demo.country_city_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walid.demo.country_city_service.dto.CityDto;
import com.walid.demo.country_city_service.service.CityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CityController {

    private final CityService service;

    @GetMapping("/countries/{countryId}/cities")
    public Page<CityDto> getCities(
            @PathVariable Long countryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getCitiesByCountry(countryId, PageRequest.of(page, size));
    }

    @GetMapping("/cities/{id}")
    public CityDto getCity(@PathVariable Long id) {
        return service.getCityById(id);
    }
}
