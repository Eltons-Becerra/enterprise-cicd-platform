import type { ServiceHealth } from "../types/ServiceHealth";

async function getJson<T>(url: string): Promise<T> {
  const response = await fetch(url);

  if (!response.ok) {
    throw new Error(
      `La solicitud a ${url} falló con código ${response.status}`,
    );
  }

  return response.json() as Promise<T>;
}

export function getJavaHealth(): Promise<ServiceHealth> {
  return getJson<ServiceHealth>(
    "/java-api/api/service/health",
  );
}

export function getPythonHealth(): Promise<ServiceHealth> {
  return getJson<ServiceHealth>(
    "/python-api/api/python/health",
  );
}