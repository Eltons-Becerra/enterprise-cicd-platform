pipeline {
    agent none

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {
        stage('Java Validation') {
            agent {
                label 'java17'
            }

            steps {
                checkoutCode()

                dir('backend-java') {
                    sh '''
                        echo "Validando backend Java"
                        java -version
                        mvn -version
                        mvn clean test
                    '''
                }
            }
        }

        stage('Python Validation') {
            agent {
                label 'python'
            }

            steps {
                checkoutCode()

                dir('backend-python') {
                    sh '''
                        echo "Validando backend Python"
                        python3 --version

                        rm -rf .venv
                        python3 -m venv .venv
                        . .venv/bin/activate

                        python -m pip install --upgrade pip
                        pip install -r requirements-dev.txt
                        pytest -v
                    '''
                }
            }
        }

        stage('Frontend Validation') {
            agent {
                label 'node22'
            }

            steps {
                checkoutCode()

                dir('frontend-react') {
                    sh '''
                        echo "Validando frontend React"
                        node --version
                        npm --version
                        npm ci
                        npm run build
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Todas las validaciones finalizaron correctamente.'
        }

        failure {
            echo 'Una o más validaciones fallaron.'
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

        extensions: [[
            $class: 'CloneOption',
            shallow: true,
            depth: 1,
            noTags: true,
            timeout: 10
        ]]
    ])
}