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

def commitToGit() {
   echo 'commiting to GIT'
    withCredentials([usernamePassword(credentialsId: '186a8e7d-2430-43cf-aad1-fffca7036dbc', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
       sh 'git config --global user.email "jenkins@example.com"'
       sh 'git config --global user.name "jenkins"'
       sh 'git status'
       sh 'git branch'
       sh 'git config --list'
       sh "git remote set-url origin https://${USERNAME}:${PASSWORD}@github.com/arunvuyala/myapp.git"
       sh 'git add pom.xml'
       sh 'git commit -m "commit from jenkins"'
       sh 'git push origin HEAD:master'
    }
    
}

return this
