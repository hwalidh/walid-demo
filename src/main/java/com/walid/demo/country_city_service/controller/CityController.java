package com.walid.demo.country_city_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walid.demo.country_city_service.dto.CityDto;
import com.walid.demo.country_city_service.exception.dto.ErrorResponse;
import com.walid.demo.country_city_service.service.CityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Cities", description = "City APIs")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CityController {

    private final CityService service;

    @Operation(summary = "Get cities by country with pagination")
    @GetMapping("/countries/{countryId}/cities")
    public Page<CityDto> getCities(
            @PathVariable Long countryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getCitiesByCountry(countryId, PageRequest.of(page, size));
    }

    @Operation(summary = "Get city by id")
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "City found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CityDto.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "City not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/cities/{id}")
    public CityDto getCity(@PathVariable Long id) {
        return service.getCityById(id);
    }
}
