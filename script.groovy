def buildJar() {
    echo 'building the application...'
    sh 'mvn package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                  sh 'echo $PASSWORD | docker login -u $USERNAME --password-stdin'
                  sh 'docker build -t myapp:latest .'
                  sh 'docker tag myapp:latest vuyalaarun/myapp:latest'
                  sh 'docker push vuyalaarun/myapp:latest'
    }
}

def deployApp() {
    echo 'deploying the application...'
}

return this
