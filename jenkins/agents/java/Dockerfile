FROM jenkins/ssh-agent:latest-jdk17

USER root

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
       git \
       maven \
       curl \
       ca-certificates \
       openssh-client \
    && rm -rf /var/lib/apt/lists/*

# El entrypoint setup-sshd necesita iniciar como root.
USER root