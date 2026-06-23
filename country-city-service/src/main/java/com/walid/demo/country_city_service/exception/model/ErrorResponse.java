package com.walid.demo.country_city_service.exception.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error response structure")
public record ErrorResponse(

        @Schema(example = "404") int status,

        @Schema(example = "404") ErrorCode error,

        @Schema(example = "City not found") String message,

        @Schema(example = "/cities/1") String path,

        @Schema(example = "1719050000000") long timestamp,

        @Schema(example = "a3f1c9d2-xxxx-xxxx") String correlationId) {
}