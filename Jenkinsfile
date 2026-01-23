pipeline {   
    agent any
    stages {
        stage("test") {
            steps {
                script {
                    echo "running tests..."
                    echo "executing unit tests...for branch ${BRANCH_NAME} "
                }
            }
        }
        stage("build") {
            when {
                BRANCH_NAME == 'main'
            }
            steps {
                script {
                    echo "building the application..."

                }
            }
        }

        stage("deploy") {
            steps {
                script {
                    echo "deploying the application..."
                }
            }
        }               
    }
} 
