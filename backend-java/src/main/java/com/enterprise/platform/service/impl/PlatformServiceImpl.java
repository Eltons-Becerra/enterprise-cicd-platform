package com.enterprise.platform.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.enterprise.platform.dto.HealthResponse;
import com.enterprise.platform.dto.ServiceInfoResponse;

@Service
public class PlatformServiceImpl implements PlatformService {

    @Value("${spring.application.name:enterprise-java-api}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.environment:development}")
    private String environment;

    @Override
    public HealthResponse health() {
        return new HealthResponse("java-api", "UP", environment);
    }

    @Override
    public ServiceInfoResponse info() {
        return new ServiceInfoResponse(
                appName,
                appVersion,
                environment,
                "Java + Spring Boot"
        );
    }
}