pipeline {   
    agent any
    tools {
        maven 'maven-3.9'
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage("build jar") {
            steps {
                script {
                    gv.buildJar()

                }
            }
        }

        stage("build image") {
            steps {
              echo "Building Image"
              withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                  sh 'echo $PASSWORD | docker login -u $USERNAME --password-stdin'
                  sh 'docker build -t myapp:latest .'
                  sh 'docker tag myapp:latest vuyalaarun/myapp:latest'
                  sh 'docker push vuyalaarun/myapp:latest'
            }
        }

        stage("deploy") {
            steps {
              echo "Deploying app Image"
            }
        }               
    }
} 
