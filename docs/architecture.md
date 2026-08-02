# Arquitectura de la plataforma

## Descripción

La plataforma utiliza una arquitectura basada en servicios contenerizados.

NGINX funciona como gateway y constituye el único punto de entrada público.

## Flujo de una solicitud al frontend

```text
Navegador
    |
    v
NGINX Gateway
    |
    v
Frontend React

Contenido:

# Arquitectura de la plataforma

## Descripción

La plataforma utiliza una arquitectura basada en servicios contenerizados.

NGINX funciona como gateway y constituye el único punto de entrada público.

## Flujo de una solicitud al frontend

```text
Navegador
    |
    v
NGINX Gateway
    |
    v
Frontend React

Comunicación Java con Python

Java API
    |
    | http://python-api:8000
    v
Python API

El nombre python-api es resuelto por el DNS interno de Docker Compose.

Red

Todos los servicios pertenecen a:

enterprise-network

Puertos internos


| Servicio   | Puerto interno |
| ---------- | -------------: |
| Gateway    |             80 |
| Frontend   |             80 |
| Java API   |           8080 |
| Python API |           8000 |

Exposición pública

Solo el gateway publica un puerto hacia el host:

Host:80 -> Gateway:80