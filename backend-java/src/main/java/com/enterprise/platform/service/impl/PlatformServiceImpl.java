package com.enterprise.platform.service.impl;

import com.enterprise.platform.dto.HealthResponse;
import com.enterprise.platform.dto.ServiceInfoResponse;
import com.enterprise.platform.service.PlatformService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PlatformServiceImpl implements PlatformService {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${app.version}")
    private String applicationVersion;

    @Value("${app.environment}")
    private String environment;

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

}