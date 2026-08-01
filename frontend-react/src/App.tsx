import { useEffect, useState } from "react";
import "./App.css";

import {
  getJavaHealth,
  getPythonHealth,
} from "./services/platformApi";

import type { ServiceHealth } from "./types/ServiceHealth";

interface ServiceCardProps {
  title: string;
  description: string;
  health: ServiceHealth | null;
  loading: boolean;
  error: string | null;
}

function ServiceCard({
  title,
  description,
  health,
  loading,
  error,
}: ServiceCardProps) {
  const isUp = health?.status === "UP";

  return (
    <article className="service-card">
      <div className="service-card-header">
        <div>
          <p className="service-label">Servicio</p>
          <h2>{title}</h2>
        </div>

        <span
          className={
            loading
              ? "status-indicator status-loading"
              : isUp
                ? "status-indicator status-up"
                : "status-indicator status-down"
          }
          aria-label={
            loading
              ? "Consultando"
              : isUp
                ? "Servicio activo"
                : "Servicio inactivo"
          }
        />
      </div>

      <p className="service-description">{description}</p>

      {loading && (
        <div className="service-message">
          Consultando servicio...
        </div>
      )}

      {!loading && error && (
        <div className="service-message error-message">
          {error}
        </div>
      )}

      {!loading && !error && health && (
        <dl className="service-details">
          <div>
            <dt>Nombre técnico</dt>
            <dd>{health.service}</dd>
          </div>

          <div>
            <dt>Estado</dt>
            <dd>
              <span
                className={
                  isUp
                    ? "status-badge status-badge-up"
                    : "status-badge status-badge-down"
                }
              >
                {health.status}
              </span>
            </dd>
          </div>

          <div>
            <dt>Ambiente</dt>
            <dd>{health.environment}</dd>
          </div>
        </dl>
      )}
    </article>
  );
}

function App() {
  const [javaHealth, setJavaHealth] =
    useState<ServiceHealth | null>(null);

  const [pythonHealth, setPythonHealth] =
    useState<ServiceHealth | null>(null);

  const [javaError, setJavaError] =
    useState<string | null>(null);

  const [pythonError, setPythonError] =
    useState<string | null>(null);

  const [loading, setLoading] = useState(true);

  const [lastUpdated, setLastUpdated] =
    useState<Date | null>(null);

  async function loadPlatformStatus() {
    setLoading(true);
    setJavaError(null);
    setPythonError(null);

    const [javaResult, pythonResult] =
      await Promise.allSettled([
        getJavaHealth(),
        getPythonHealth(),
      ]);

    if (javaResult.status === "fulfilled") {
      setJavaHealth(javaResult.value);
    } else {
      setJavaHealth(null);
      setJavaError(
        "No fue posible consultar la API Java.",
      );
    }

    if (pythonResult.status === "fulfilled") {
      setPythonHealth(pythonResult.value);
    } else {
      setPythonHealth(null);
      setPythonError(
        "No fue posible consultar la API Python.",
      );
    }

    setLastUpdated(new Date());
    setLoading(false);
  }

  useEffect(() => {
    void loadPlatformStatus();
  }, []);

  const servicesUp =
    javaHealth?.status === "UP" &&
    pythonHealth?.status === "UP";

  const hasErrors =
    javaError !== null || pythonError !== null;

  const platformStatus = loading
    ? "CHECKING"
    : servicesUp
      ? "UP"
      : hasErrors
        ? "DEGRADED"
        : "DOWN";

  return (
    <main className="dashboard">
      <header className="dashboard-header">
        <div>
          <p className="eyebrow">
            Enterprise CI/CD Platform
          </p>

          <h1>Estado de la plataforma</h1>

          <p className="header-description">
            Supervisión centralizada de los servicios
            desarrollados en Java y Python.
          </p>
        </div>

        <div
          className={`platform-summary platform-${platformStatus.toLowerCase()}`}
        >
          <span className="summary-label">
            Estado general
          </span>

          <strong>{platformStatus}</strong>
        </div>
      </header>

      <section className="metrics-grid">
        <article className="metric-card">
          <span>Servicios registrados</span>
          <strong>2</strong>
        </article>

        <article className="metric-card">
          <span>Servicios disponibles</span>
          <strong>
            {
              [
                javaHealth?.status === "UP",
                pythonHealth?.status === "UP",
              ].filter(Boolean).length
            }
          </strong>
        </article>

        <article className="metric-card">
          <span>Ambiente</span>
          <strong>
            {javaHealth?.environment ??
              pythonHealth?.environment ??
              "Sin información"}
          </strong>
        </article>
      </section>

      <section className="services-section">
        <div className="section-heading">
          <div>
            <p className="section-label">
              Microservicios
            </p>
            <h2>Servicios supervisados</h2>
          </div>

          <button
            className="refresh-button"
            type="button"
            onClick={() => void loadPlatformStatus()}
            disabled={loading}
          >
            {loading
              ? "Actualizando..."
              : "Actualizar estado"}
          </button>
        </div>

        <div className="services-grid">
          <ServiceCard
            title="Backend Java"
            description="API empresarial desarrollada con Spring Boot."
            health={javaHealth}
            loading={loading}
            error={javaError}
          />

          <ServiceCard
            title="Backend Python"
            description="Servicio desarrollado con Python y FastAPI."
            health={pythonHealth}
            loading={loading}
            error={pythonError}
          />
        </div>
      </section>

      <footer className="dashboard-footer">
        <span>
          Última actualización:
          {" "}
          {lastUpdated
            ? lastUpdated.toLocaleTimeString()
            : "Pendiente"}
        </span>

        <span>
          Docker Compose · React · Java · Python
        </span>
      </footer>
    </main>
  );
}

export default App;