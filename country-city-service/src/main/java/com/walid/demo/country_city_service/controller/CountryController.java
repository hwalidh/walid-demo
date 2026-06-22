package com.walid.demo.country_city_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walid.demo.country_city_service.dto.CountryDto;
import com.walid.demo.country_city_service.service.CountryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Countries", description = "Country APIs")
@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService service;

    @Operation(summary = "Get all countries")
    @ApiResponse(responseCode = "200", description = "List of countries")
    @GetMapping
    public List<CountryDto> getCountries() {
        return service.getAllCountries();
    }
}
