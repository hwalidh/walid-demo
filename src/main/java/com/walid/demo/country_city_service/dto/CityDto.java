package com.walid.demo.country_city_service.dto;

import jakarta.validation.constraints.NotBlank;

public record CityDto(
    Long id, 
    @NotBlank(message = "City name is required")  String name, 
    Integer population, 
    String description) {}
