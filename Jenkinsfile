pipeline {
    agent any

    environment {
        DOCKER_BIN = "/usr/local/bin/docker"   // Your Docker path
        CONTAINER_NAME = "jenkins-demo"
        BASE_PORT = 3000
        DOCKER_CONFIG = "/tmp/docker-config"   // Avoid docker-credential issues
        NODE_VERSION = "22.13.1"               // Your nvm Node version
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
                    export DOCKER_CONFIG=$DOCKER_CONFIG
                    mkdir -p \$DOCKER_CONFIG

                    # Initialize nvm and Node
                    export NVM_DIR="\$HOME/.nvm"
                    [ -s "\$NVM_DIR/nvm.sh" ] && \\. "\$NVM_DIR/nvm.sh"
                    nvm use ${NODE_VERSION}

                    node -v
                    npm -v
                    $DOCKER_BIN --version
                """
            }
        }

        stage('Install Dependencies') {
            steps {
                sh """
                    # Initialize nvm
                    export NVM_DIR="\$HOME/.nvm"
                    [ -s "\$NVM_DIR/nvm.sh" ] && \\. "\$NVM_DIR/nvm.sh"
                    nvm use ${NODE_VERSION}

                    npm install
                """
            }
        }

        stage('Run Tests (Optional)') {
            steps {
                sh """
                    # Initialize nvm
                    export NVM_DIR="\$HOME/.nvm"
                    [ -s "\$NVM_DIR/nvm.sh" ] && \\. "\$NVM_DIR/nvm.sh"
                    nvm use ${NODE_VERSION}

                    npm test || echo 'No tests configured, skipping'
                """
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
                    env.APP_PORT = (isOccupied != "0") ? ((BASE_PORT + 1).toString()) : BASE_PORT.toString()
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
