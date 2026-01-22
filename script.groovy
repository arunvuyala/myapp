def buildJar() {
    echo 'building the application...'
    sh 'mvn package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                  sh 'echo $PASSWORD | docker login -u $USERNAME --password-stdin'
                  sh 'docker build -t myapp:2.0 .'
                  sh 'docker tag myapp:2.0 vuyalaarun/myapp:2.0'
                  sh 'docker push vuyalaarun/myapp:2.0'
    }
}

def deployApp() {
    echo 'deploying the application...'
}

return this
