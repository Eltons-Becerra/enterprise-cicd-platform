pipeline {
    agent none

    options {
        timestamps()
        disableConcurrentBuilds()

        buildDiscarder(
            logRotator(
                numToKeepStr: '10',
                artifactNumToKeepStr: '5'
            )
        )
    }

    stages {

        stage('Validate Components') {
            failFast true

            parallel {

                stage('Java Build') {
                    agent { label 'java17' }

                    steps {
                        checkoutCode()

                        dir('backend-java') {
                            sh '''
                                echo "================================="
                                echo "Compilando backend Java"
                                echo "================================="

                                java -version
                                mvn -version

                                mvn clean verify
                            '''
                        }
                    }

                    post {
                        always {
                            junit(
                                testResults: 'backend-java/target/surefire-reports/*.xml',
                                allowEmptyResults: true
                            )
                        }

                        success {
                            archiveArtifacts(
                                artifacts: 'backend-java/target/app.jar',
                                fingerprint: true
                            )
                        }
                    }
                }

                stage('Python Tests') {
                    agent { label 'python' }

                    steps {
                        checkoutCode()

                        dir('backend-python') {
                            sh '''
                                echo "================================="
                                echo "Validando backend Python"
                                echo "================================="

                                python3 --version

                                rm -rf .venv reports

                                python3 -m venv .venv
                                . .venv/bin/activate

                                python -m pip install --upgrade pip
                                pip install -r requirements-dev.txt

                                mkdir -p reports

                                pytest \
                                  --junitxml=reports/pytest-results.xml \
                                  -v
                            '''
                        }
                    }

                    post {
                        always {
                            junit(
                                testResults: 'backend-python/reports/pytest-results.xml',
                                allowEmptyResults: true
                            )
                        }
                    }
                }

                stage('Frontend Build') {
                    agent { label 'node22' }

                    steps {
                        checkoutCode()

                        dir('frontend-react') {
                            sh '''
                                echo "================================="
                                echo "Compilando frontend React"
                                echo "================================="

                                node --version
                                npm --version

                                npm ci
                                npm run build
                            '''
                        }
                    }

                    post {
                        success {
                            archiveArtifacts(
                                artifacts: 'frontend-react/dist/**/*',
                                fingerprint: true
                            )
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            agent { label 'docker-builder' }

            steps {
                checkoutCode()

                sh '''
                    echo "===== Usuario ====="
                    whoami

                    echo "===== Grupos ====="
                    id

                    echo "===== Socket ====="
                    ls -ln /var/run/docker.sock

                    echo "===== Docker ====="
                    which docker

                    docker version || true
                '''

                sh '''
                    echo "========================================"
                    echo "Construcción de imágenes Docker"
                    echo "Build de Jenkins: ${BUILD_NUMBER}"
                    echo "========================================"

                    echo "Construyendo imagen del backend Java..."
                    docker build \
                      --tag enterprise-java-api:${BUILD_NUMBER} \
                      --tag enterprise-java-api:latest \
                      ./backend-java

                    echo "Construyendo imagen del backend Python..."
                    docker build \
                      --tag enterprise-python-api:${BUILD_NUMBER} \
                      --tag enterprise-python-api:latest \
                      ./backend-python

                    echo "Construyendo imagen del frontend React..."
                    docker build \
                      --tag enterprise-frontend:${BUILD_NUMBER} \
                      --tag enterprise-frontend:latest \
                      ./frontend-react

                    echo "========================================"
                    echo "Validando imágenes construidas"
                    echo "========================================"

                    docker image inspect \
                      enterprise-java-api:${BUILD_NUMBER} \
                      enterprise-python-api:${BUILD_NUMBER} \
                      enterprise-frontend:${BUILD_NUMBER} \
                      > docker-images.json

                    echo "Imágenes creadas correctamente:"
                    docker image ls \
                      --format "table {{.Repository}}\\t{{.Tag}}\\t{{.Size}}" \
                      | grep enterprise || true
                '''

                archiveArtifacts(
                    artifacts: 'docker-images.json',
                    fingerprint: true
                )
            }
        }

        stage('Smoke Test Docker Images') {
            agent { label 'docker-builder' }

            steps {
                sh '''
                    set -e

                    echo "========================================"
                    echo " Pruebas de humo de las imágenes Docker"
                    echo " Build de Jenkins: ${BUILD_NUMBER}"
                    echo "========================================"

                    NETWORK_NAME="enterprise-smoke-network"

                    PYTHON_CONTAINER="enterprise-python-api-smoke"
                    JAVA_CONTAINER="enterprise-java-api-smoke"
                    FRONTEND_CONTAINER="enterprise-frontend-smoke"

                    cleanup() {
                        echo "Eliminando contenedores temporales..."

                        docker rm -f \
                          "${FRONTEND_CONTAINER}" \
                          "${JAVA_CONTAINER}" \
                          "${PYTHON_CONTAINER}" \
                          2>/dev/null || true

                        docker network rm \
                          "${NETWORK_NAME}" \
                          2>/dev/null || true
                    }

                    trap cleanup EXIT

                    cleanup

                    docker network create "${NETWORK_NAME}"

                    echo "========================================"
                    echo " Iniciando API Python"
                    echo "========================================"

                    docker run -d \
                      --name "${PYTHON_CONTAINER}" \
                      --network "${NETWORK_NAME}" \
                      -p 18000:8000 \
                      -e ENVIRONMENT=test \
                      enterprise-python-api:${BUILD_NUMBER}

                    echo "Esperando la API Python..."

                    for attempt in $(seq 1 20); do
                        if curl -fsS \
                          http://host.docker.internal:18000/api/python/health \
                          > python-health.json; then
                            echo "API Python disponible."
                            break
                        fi

                        if [ "${attempt}" -eq 20 ]; then
                            echo "La API Python no respondió correctamente."
                            docker logs "${PYTHON_CONTAINER}"
                            exit 1
                        fi

                        sleep 3
                    done

                    cat python-health.json

                    echo "========================================"
                    echo " Iniciando API Java"
                    echo "========================================"

                    docker run -d \
                      --name "${JAVA_CONTAINER}" \
                      --network "${NETWORK_NAME}" \
                      -p 18080:8080 \
                      -e ENVIRONMENT=test \
                      -e PYTHON_API_URL=http://${PYTHON_CONTAINER}:8000 \
                      enterprise-java-api:${BUILD_NUMBER}

                    echo "Esperando la API Java..."

                    for attempt in $(seq 1 30); do
                        if curl -fsS \
                          http://host.docker.internal:18080/api/service/health \
                          > java-health.json; then
                            echo "API Java disponible."
                            break
                        fi

                        if [ "${attempt}" -eq 30 ]; then
                            echo "La API Java no respondió correctamente."
                            docker logs "${JAVA_CONTAINER}"
                            exit 1
                        fi

                        sleep 3
                    done

                    cat java-health.json

                    echo "Validando integración Java con Python..."

                    curl -fsS \
                      http://host.docker.internal:18080/api/java/platform-status \
                      > platform-status.json

                    cat platform-status.json

                    grep -q '"platformStatus":"UP"' platform-status.json

                    echo "========================================"
                    echo " Iniciando frontend React"
                    echo "========================================"

                    docker run -d \
                      --name "${FRONTEND_CONTAINER}" \
                      --network "${NETWORK_NAME}" \
                      -p 13000:80 \
                      enterprise-frontend:${BUILD_NUMBER}

                    echo "Esperando el frontend..."

                    for attempt in $(seq 1 20); do
                        if curl -fsS \
                          http://host.docker.internal:13000 \
                          > frontend-index.html; then
                            echo "Frontend disponible."
                            break
                        fi

                        if [ "${attempt}" -eq 20 ]; then
                            echo "El frontend no respondió correctamente."
                            docker logs "${FRONTEND_CONTAINER}"
                            exit 1
                        fi

                        sleep 3
                    done

                    grep -qi '<div id="root">' frontend-index.html

                    echo "========================================"
                    echo " Todas las imágenes pasaron las pruebas"
                    echo "========================================"
                '''

                archiveArtifacts(
                    artifacts: '*.json,frontend-index.html',
                    fingerprint: true
                )
            }
        }
    }

    post {
        success {
            echo "La integración continua finalizó correctamente."
            echo "Los artefactos Java y React quedaron almacenados en Jenkins."
            echo "Las imágenes Docker del build ${BUILD_NUMBER} fueron creadas correctamente."
        }

        failure {
            echo "La integración continua falló."
            echo "Revisa la etapa y el reporte de pruebas correspondiente."
        }
    }
}

void checkoutCode() {
    checkout([
        $class: 'GitSCM',

        branches: [[ name: '*/main' ]],

        userRemoteConfigs: [[
            url: 'git@github.com:Eltons-Becerra/enterprise-cicd-platform.git',
            credentialsId: 'github-checkout-ssh'
        ]],

        extensions: [
            [
                $class: 'CloneOption',
                shallow: true,
                depth: 1,
                noTags: true,
                timeout: 10
            ],
            [
                $class: 'CleanBeforeCheckout'
            ]
        ]
    ])
}
