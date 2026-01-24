def incrementVersion() {
    echo 'Incrementing version...'

    // Use bash to run Maven
    sh '''
        #!/bin/bash
        mvn build-helper:parse-version versions:set \
            -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
            versions:commit -q -DgenerateBackupPoms=false
    '''

    // Read version from pom.xml
    def pomContent = readFile('pom.xml')
    def matcher = pomContent =~ /<version>(.+)<\/version>/
    def newVersion = matcher[0][1]

    env.IMAGE_VERSION = newVersion
    echo "IMAGE_VERSION=${env.IMAGE_VERSION}"
}


def buildJar() {
    echo 'building the application...'
    sh 'mvn clean package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                  sh 'echo $PASSWORD | docker login -u $USERNAME --password-stdin'
                  sh "docker build -t myapp:${env.IMAGE_VERSION} ."
                  sh "docker tag myapp:${env.IMAGE_VERSION} vuyalaarun/myapp:${env.IMAGE_VERSION}"
                  sh "docker push vuyalaarun/myapp:${env.IMAGE_VERSION}"
    }
}

def deployApp() {
    echo 'deploying the application...'
}

return this
