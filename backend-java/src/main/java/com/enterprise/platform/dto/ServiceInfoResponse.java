package com.enterprise.platform.dto;

public record ServiceInfoResponse(
        String name,
        String version,
        String environment,
        String technology
) {
}