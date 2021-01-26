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
                            write-host $PSVersionTable.PSVersion
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
                stage('Test On Windows 2')
                    agent {
                        label "windows10"
                    }
                    steps {
                        powershell '''
                            write-host "Stage2"
                        '''
                    }
            }
        }
    }
}
