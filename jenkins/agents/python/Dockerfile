FROM jenkins/ssh-agent:latest-jdk17

USER root

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
       git \
       curl \
       ca-certificates \
       openssh-client \
       python3 \
       python3-pip \
       python3-venv \
    && rm -rf /var/lib/apt/lists/*

RUN python3 --version \
    && pip3 --version

# Evita usar pip globalmente; los pipelines crearán un entorno virtual.
USER root