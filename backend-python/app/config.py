import os


class Settings:
    """Configuración general de la API."""

    app_name: str = os.getenv(
        "APP_NAME",
        "Enterprise CI/CD Python API",
    )

    app_version: str = os.getenv(
        "APP_VERSION",
        "1.0.0",
    )

    environment: str = os.getenv(
        "ENVIRONMENT",
        "development",
    )


settings = Settings()
