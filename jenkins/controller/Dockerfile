FROM jenkins/jenkins:lts-jdk17

USER root

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
       openssh-client \
       git \
       ca-certificates \
    && rm -rf /var/lib/apt/lists/*

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN jenkins-plugin-cli \
    --plugin-file /usr/share/jenkins/ref/plugins.txt

COPY casc /var/jenkins_casc

RUN chown -R jenkins:jenkins /var/jenkins_casc

USER jenkins