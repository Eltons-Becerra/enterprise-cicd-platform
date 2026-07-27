from pydantic import BaseModel


class HealthResponse(BaseModel):
    service: str
    status: str
    environment: str


class ServiceInfoResponse(BaseModel):
    name: str
    version: str
    environment: str
    technology: str
