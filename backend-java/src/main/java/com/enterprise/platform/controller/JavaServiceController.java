package com.enterprise.platform.controller;

import com.enterprise.platform.dto.PlatformStatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class JavaServiceController {

    @Value("${spring.application.name:enterprise-java-api}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.environment:development}")
    private String environment;

    @GetMapping("/api/java/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("service", "java-api");
        response.put("status", "UP");
        response.put("environment", environment);
        return response;
    }

    @GetMapping("/api/java/info")
    public Map<String, String> info() {
        Map<String, String> response = new HashMap<>();
        response.put("name", appName);
        response.put("version", appVersion);
        response.put("environment", environment);
        response.put("technology", "Java + Spring Boot");
        return response;
        }
        @GetMapping("/api/java/platform-status")
        public PlatformStatusResponse platformStatus() {
        return platformService.platformStatus();
        }
}
