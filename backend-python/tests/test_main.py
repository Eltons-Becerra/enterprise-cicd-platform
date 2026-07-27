from fastapi.testclient import TestClient

from app.main import app


client = TestClient(app)


def test_root() -> None:
    response = client.get("/")

    assert response.status_code == 200
    assert response.json()["message"] == "Enterprise CI/CD Python API"


def test_health_check() -> None:
    response = client.get("/api/python/health")

    assert response.status_code == 200

    data = response.json()

    assert data["service"] == "python-api"
    assert data["status"] == "UP"
    assert data["environment"] == "development"


def test_service_info() -> None:
    response = client.get("/api/python/info")

    assert response.status_code == 200

    data = response.json()

    assert data["technology"] == "Python + FastAPI"
    assert data["version"] == "1.0.0"
