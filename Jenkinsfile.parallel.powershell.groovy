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
                            Start-Sleep -Seconds 5
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
                stage('Test On Windows 2') {
                    agent {
                        label "windows10"
                    }
                    steps {
                        powershell '''
                            write-host "Stage2"
                        '''
                    }
                }   
                stage('Run another job') {
                    steps {
                        build job: 'Job_With_Parameters-pipeline', 
                            parameters: [[$class: 'StringParameterValue', name: 'String', value: 'Run another job: wait false']], wait: false
                        build job: 'Job_With_Parameters-pipeline', 
                            parameters:[
                                string(name: 'String', value: 'testing' ),
                                string(name: 'SleepTime', value: 60)
                                //[$class: 'StringParameterValue', name: 'String', value: 'Run another job: wait true']                              
                            ],
                            wait: true
                    }
                }                
            }
        }
    }
}
