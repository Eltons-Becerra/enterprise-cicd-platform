package com.enterprise.platform.controller;

import com.enterprise.platform.dto.HealthResponse;
import com.enterprise.platform.dto.ServiceInfoResponse;
import com.enterprise.platform.exception.ResourceNotFoundException;
import com.enterprise.platform.service.PlatformService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ServiceController {

    private final PlatformService platformService;

    public ServiceController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "message", "Enterprise CI/CD Java API",
                "status", "running"
        );
    }

    @GetMapping("/api/java/health")
    public HealthResponse health() {
        return platformService.health();
    }

    @GetMapping("/api/java/info")
    public ServiceInfoResponse info() {
        return platformService.info();
    }

    @GetMapping("/api/java/resources/{id}")
    public Map<String, Object> findResource(@PathVariable Long id) {

        if (id != 1L) {
            throw new ResourceNotFoundException(
                    "No se encontró el recurso con identificador " + id
            );
        }

        return Map.of(
                "id", id,
                "name", "Enterprise resource",
                "status", "active"
        );
    }
}