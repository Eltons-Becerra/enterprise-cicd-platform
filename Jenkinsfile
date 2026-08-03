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
                    agent {
                        label 'java17'
                    }

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
                    agent {
                        label 'python'
                    }

                    steps {
                        checkoutCode()

                        dir('backend-python') {
                            sh '''
                                echo "================================="
                                echo "Validando backend Python"
                                echo "================================="

                                python3 --version

                                rm -rf .venv
                                python3 -m venv .venv

                                . .venv/bin/activate

                                python -m pip install --upgrade pip
                                pip install -r requirements-dev.txt
                                mkdir -p reports

                                pytest \
                                --junitxml=reports/pytest-results.xml \
                                -v

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
                    agent {
                        label 'node22'
                    }

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
    }

    post {
        success {
            echo 'La integración continua finalizó correctamente.'
            echo 'Los artefactos Java y React quedaron almacenados en Jenkins.'
        }

        failure {
            echo 'La integración continua falló.'
            echo 'Revisa la etapa y el reporte de pruebas correspondiente.'
        }
    }
}

void checkoutCode() {
    checkout([
        $class: 'GitSCM',

        branches: [[
            name: '*/main'
        ]],

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