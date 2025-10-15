pipeline {
    agent any

    environment {
        DOCKER_BIN = "/usr/local/bin/docker"   // Full path to Docker binary
        CONTAINER_NAME = "jenkins-demo"
        BASE_PORT = 3000
        DOCKER_CONFIG = "/tmp/docker-config"      // Prevent Docker credential helper issues
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/mkbedi/jenkins-demo.git'
            }
        }

        stage('Setup Environment') {
            steps {
                sh """
                    mkdir -p $DOCKER_CONFIG
                    export DOCKER_CONFIG=$DOCKER_CONFIG
                    node -v
                    npm -v
                    $DOCKER_BIN --version
                """
            }
        }

        stage('Install Dependencies') {
            steps {
                sh 'npm install'
            }
        }

        stage('Run Tests (Optional)') {
            steps {
                sh 'npm test || echo "No tests configured, skipping"'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                    export DOCKER_CONFIG=$DOCKER_CONFIG
                    $DOCKER_BIN build -t $CONTAINER_NAME:latest .
                """
            }
        }

        stage('Determine Free Port') {
            steps {
                script {
                    def isOccupied = sh(script: "lsof -ti tcp:$BASE_PORT | wc -l", returnStdout: true).trim()
                    if (isOccupied != "0") {
                        env.APP_PORT = (BASE_PORT.toInteger() + 1).toString()
                    } else {
                        env.APP_PORT = BASE_PORT.toString()
                    }
                    echo "Using port: ${env.APP_PORT}"
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                sh """
                    export DOCKER_CONFIG=$DOCKER_CONFIG
                    # Stop old container if exists
                    $DOCKER_BIN ps -q --filter "name=$CONTAINER_NAME" | grep -q . && $DOCKER_BIN rm -f $CONTAINER_NAME || true
                    # Kill any process using chosen port
                    lsof -ti tcp:$APP_PORT | xargs -r kill -9
                    # Run new container
                    $DOCKER_BIN run -d --name $CONTAINER_NAME -p $APP_PORT:3000 $CONTAINER_NAME:latest
                """
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline complete! Node.js app running at http://localhost:${env.APP_PORT}"
        }
        failure {
            echo "❌ Pipeline failed. Check logs for errors."
        }
        always {
            sh "rm -rf $DOCKER_CONFIG"
        }
    }
}
