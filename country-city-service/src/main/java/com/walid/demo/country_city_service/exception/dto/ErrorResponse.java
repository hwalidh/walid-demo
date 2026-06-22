package com.walid.demo.country_city_service.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error response structure")
public record ErrorResponse(

    @Schema(example = "404") 
    int status ,

    @Schema(example = "404") 
    String error,

    @Schema(example = "City not found") 
    String message,

    @Schema(example = "/cities/1") 
    String path,

    @Schema(example = "1719050000000") 
    long timestamp
) {}