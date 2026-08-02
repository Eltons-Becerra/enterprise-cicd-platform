FROM jenkins/ssh-agent:latest-jdk17

USER root

ARG NODE_VERSION=22.22.2

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
       git \
       curl \
       xz-utils \
       ca-certificates \
       openssh-client \
    && rm -rf /var/lib/apt/lists/*

RUN ARCH="$(dpkg --print-architecture)" \
    && case "${ARCH}" in \
         amd64) NODE_ARCH="x64" ;; \
         arm64) NODE_ARCH="arm64" ;; \
         *) echo "Arquitectura no soportada: ${ARCH}" && exit 1 ;; \
       esac \
    && curl -fsSLO \
       "https://nodejs.org/dist/v${NODE_VERSION}/node-v${NODE_VERSION}-linux-${NODE_ARCH}.tar.xz" \
    && tar -xJf \
       "node-v${NODE_VERSION}-linux-${NODE_ARCH}.tar.xz" \
       -C /usr/local \
       --strip-components=1 \
    && rm "node-v${NODE_VERSION}-linux-${NODE_ARCH}.tar.xz" \
    && node --version \
    && npm --version

# setup-sshd debe iniciar como root.
USER root