package com.enterprise.platform.dto;

public record HealthResponse(
        String service,
        String status,
        String environment
) {
}