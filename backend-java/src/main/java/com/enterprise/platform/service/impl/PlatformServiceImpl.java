package com.enterprise.platform.service.impl;

import com.enterprise.platform.dto.HealthResponse;
import com.enterprise.platform.dto.PlatformStatusResponse;
import com.enterprise.platform.dto.PythonHealthResponse;
import com.enterprise.platform.dto.ServiceInfoResponse;
import com.enterprise.platform.service.PlatformService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PlatformServiceImpl implements PlatformService {

    private final RestClient restClient;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${app.version}")
    private String applicationVersion;

    @Value("${app.environment}")
    private String environment;

    @Value("${PYTHON_API_URL:http://localhost:8000}")
    private String pythonApiUrl;

    public PlatformServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public HealthResponse health() {
        return new HealthResponse(
                "java-api",
                "UP",
                environment
        );
    }

    @Override
    public ServiceInfoResponse info() {
        return new ServiceInfoResponse(
                applicationName,
                applicationVersion,
                environment,
                "Spring Boot 3 + Java 17"
        );
    }

    @Override
    public PlatformStatusResponse platformStatus() {

        HealthResponse javaHealth = health();

        PythonHealthResponse pythonHealth = restClient
                .get()
                .uri(pythonApiUrl + "/api/python/health")
                .retrieve()
                .body(PythonHealthResponse.class);

        String generalStatus =
                pythonHealth != null
                && "UP".equalsIgnoreCase(javaHealth.status())
                && "UP".equalsIgnoreCase(pythonHealth.status())
                        ? "UP"
                        : "DEGRADED";

        return new PlatformStatusResponse(
                javaHealth,
                pythonHealth,
                generalStatus
        );
    }
}