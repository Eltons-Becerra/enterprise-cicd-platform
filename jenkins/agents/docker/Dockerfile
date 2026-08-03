FROM jenkins/ssh-agent:latest-jdk17

USER root

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
       git \
       curl \
       ca-certificates \
       openssh-client \
       docker-cli \
       docker-compose \
    && rm -rf /var/lib/apt/lists/*

# El socket de Docker Desktop está montado como root:root.
# Se agrega Jenkins al grupo suplementario con GID 0.
RUN usermod -aG root jenkins

RUN docker --version \
    && docker compose version

# setup-sshd necesita iniciar como root.
USER root