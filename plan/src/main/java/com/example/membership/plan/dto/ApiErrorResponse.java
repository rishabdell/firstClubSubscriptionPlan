package com.example.membership.plan.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard API error response")
public record ApiErrorResponse(
        @Schema(description = "UTC timestamp when the error occurred")
        Instant timestamp,
        @Schema(description = "HTTP status code")
        int status,
        @Schema(description = "Brief error type")
        String error,
        @Schema(description = "Error details")
        String message,
        @Schema(description = "Request path that caused the error")
        String path) {
}
