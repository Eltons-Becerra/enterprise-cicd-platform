pipeline {
    agent none

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '5'))
    }

    stages {

        stage('Validate Components') {
            failFast true
            parallel {
                ... tus stages de Java, Python y Node ...
            }
        }

        stage('Build Docker Images') {
            agent { label 'docker-builder' }
            steps {
                ... tu build de imágenes ...
            }
        }

        stage('Smoke Test Docker Images') {
            agent { label 'docker-builder' }
            steps {
                ... tus pruebas de humo ...
            }
        }
    }

    post {
        success {
            echo "La integración continua finalizó correctamente."
        }
        failure {
            echo "La integración continua falló."
        }
    }
}
