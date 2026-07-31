package com.enterprise.platform.dto;

public record PlatformStatusResponse(
        HealthResponse javaApi,
        PythonHealthResponse pythonApi,
        String platformStatus
) {
}