package com.walid.demo.country_city_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Country representation returned by the API")
public record CountryDto(
    @Schema(example = "1")
    Long id, 

    @Schema(example = "France")
    String name
) {}
