def incrementVersion() {
    echo 'Incrementing version...'

    // Increment version using Maven
    sh "mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion} versions:commit -q -DgenerateBackupPoms=false"

    // Read pom.xml and extract <version>
    def pomContent = readFile('pom.xml')
    def matcher = pomContent =~ /<version>(.+)<\/version>/
    def newVersion = matcher[0][1]

    // Set as environment variable
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
                  sh 'docker build -t myapp:${env.IMAGE_VERSION} .'
                  sh 'docker tag myapp:2.0 vuyalaarun/myapp:${env.IMAGE_VERSION}'
                  sh 'docker push vuyalaarun/myapp:${env.IMAGE_VERSION}'
    }
}

def deployApp() {
    echo 'deploying the application...'
}

return this
