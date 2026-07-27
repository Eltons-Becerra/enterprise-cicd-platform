package com.enterprise.platform.controller;

import com.enterprise.platform.dto.HealthResponse;
import com.enterprise.platform.dto.ServiceInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ServiceController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${app.version}")
    private String applicationVersion;

    @Value("${app.environment}")
    private String environment;

    @GetMapping("/")
    public Map<String, String> home() {

        return Map.of(
                "message", "Enterprise CI/CD Java API",
                "status", "running"
        );
    }

    @GetMapping("/api/java/health")
    public HealthResponse health() {

        return new HealthResponse(
                "java-api",
                "UP",
                environment
        );
    }

    @GetMapping("/api/java/info")
    public ServiceInfoResponse info() {

        return new ServiceInfoResponse(
                applicationName,
                applicationVersion,
                environment,
                "Spring Boot 3 + Java 17"
        );
    }

}