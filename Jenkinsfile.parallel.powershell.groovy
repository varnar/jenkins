pipeline {
    agent none
    stages {
        stage('Run Tests') {
            parallel {
                stage('Test On Windows') {
                    agent {
                        label "windows10"
                    }
                    steps {
                        powershell '''
                            hostname
                        '''
                    }
                    post {
                        always {
                            powershell '''
                                echo post
                            '''
                        }
                    }
                }
            }
        }
    }
}