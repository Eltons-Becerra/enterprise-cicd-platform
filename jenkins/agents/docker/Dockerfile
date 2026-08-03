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

RUN docker --version \
    && docker compose version

USER root       