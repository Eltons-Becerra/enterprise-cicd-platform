package com.enterprise.platform.dto;

public record PythonHealthResponse(
        String service,
        String status,
        String environment
) {
}