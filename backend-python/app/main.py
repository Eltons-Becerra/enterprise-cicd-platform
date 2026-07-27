from fastapi import FastAPI

from app.config import settings
from app.schemas import HealthResponse, ServiceInfoResponse


app = FastAPI(
    title=settings.app_name,
    description=(
        "API demostrativa en Python para el proyecto "
        "Enterprise CI/CD Platform."
    ),
    version=settings.app_version,
)


@app.get("/")
def root() -> dict[str, str]:
    return {
        "message": "Enterprise CI/CD Python API",
        "documentation": "/docs",
    }


@app.get(
    "/api/python/health",
    response_model=HealthResponse,
    tags=["Monitoring"],
)
def health_check() -> HealthResponse:
    return HealthResponse(
        service="python-api",
        status="UP",
        environment=settings.environment,
    )


@app.get(
    "/api/python/info",
    response_model=ServiceInfoResponse,
    tags=["Service Information"],
)
def service_info() -> ServiceInfoResponse:
    return ServiceInfoResponse(
        name=settings.app_name,
        version=settings.app_version,
        environment=settings.environment,
        technology="Python + FastAPI",
    )
@app.get("/visitante")
def hola_eltons() -> dict[str, str]:
    return {"mensaje": "Hola Visitante"}
