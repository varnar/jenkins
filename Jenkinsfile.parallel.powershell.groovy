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
                            Start-Sleep -Seconds 10
                            $SleepTime = 10
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
                            Start-Sleep -Seconds 20
                            $SleepTime1 = 20
                        '''
                    }
                }  
            }
        } 
        stage('Run another job') {
            steps {
                echo "${SleepTime}"
                build job: 'Job_With_Parameters-pipeline', 
                    parameters: [[$class: 'StringParameterValue', name: 'String', value: 'Run another job: wait false']], wait: true
                build job: 'Job_With_Parameters-pipeline', 
                    parameters:[
                        string(name: 'String', value: 'testing' ),
                        string(name: 'SleepTime', value: '10')
                        ///[$class: 'StringParameterValue', name: 'String', value: 'Run another job: wait true']                              
                    ],
                    wait: true
            }
        }                
        stage('Run another job1') {
            steps {
                echo "${SleepTime1}"
                build job: 'Job_With_Parameters-pipeline', 
                    parameters: [[$class: 'StringParameterValue', name: 'String', value: 'Run another job: wait false']], wait: true
                build job: 'Job_With_Parameters-pipeline', 
                    parameters:[
                        string(name: 'String', value: 'testing' ),
                        string(name: 'SleepTime', value: '15')
                        ///[$class: 'StringParameterValue', name: 'String', value: 'Run another job: wait true']                              
                    ],
                    wait: true
            }
        }          
    }
}
