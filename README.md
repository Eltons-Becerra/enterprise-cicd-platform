# Enterprise CI/CD Platform

Plataforma demostrativa orientada a la implementación de una arquitectura moderna de desarrollo, contenerización, integración continua y despliegue automatizado.

## Objetivo

Este proyecto demuestra la integración de diferentes tecnologías dentro de una plataforma empresarial:

- React y TypeScript
- Java 17 y Spring Boot
- Python y FastAPI
- Docker y Docker Compose
- NGINX como API Gateway y reverse proxy
- Jenkins para CI/CD
- Kubernetes y Terraform en fases posteriores

## Arquitectura actual

```text
Navegador
    |
    v
NGINX Gateway :80
    |
    +-- /                 --> Frontend React
    +-- /java-api/        --> Backend Java
    +-- /python-api/      --> Backend Python
    +-- /api/java/        --> Backend Java

    Los servicios internos se comunican mediante una red Docker privada.

Componentes
Frontend React

Dashboard para supervisar el estado de los servicios Java y Python.

Tecnologías:

React
TypeScript
Vite
NGINX
Backend Java

API desarrollada con Spring Boot.

Endpoints principales:

GET /api/service/health
GET /api/service/info
GET /api/java/platform-status
GET /actuator/health

Backend Python

API desarrollada con FastAPI.

Endpoints principales:

GET /
GET /api/python/health
GET /api/python/info
GET /docs

NGINX Gateway

Único punto de entrada público de la plataforma.

Responsabilidades:

Publicar el frontend
Enrutar solicitudes hacia Java
Enrutar solicitudes hacia Python
Mantener los servicios internos sin exposición directa

Estructura del proyecto:

enterprise-cicd-platform/
├── backend-java/
├── backend-python/
├── frontend-react/
├── nginx/
├── jenkins/
├── kubernetes/
├── terraform/
├── monitoring/
├── docs/
├── docker-compose.yml
├── .env.example
└── README.md

Requisitos
Docker
Docker Compose
Git

Para desarrollo local también se requieren:

Java 17
Maven
Python 3.12
Node.js
Ejecución con Docker Compose

Crear el archivo .env a partir del ejemplo:







## Autor

Eltons Becerra  
Software Development Lead
