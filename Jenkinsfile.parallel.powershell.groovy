SleepTime1 = 0
SleepTime = 0

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
                        script {
                            SleepTime = powershell (returnStdout: true, script: '''
                                write-host 10
                                hostname
                                write-host $PSVersionTable.PSVersion
                                Start-Sleep -Seconds 10
                                
                            ''' )
                        }
                        println SleepTime
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
                        '''
                        script {
                            SleepTime1 = 20
                        }
                    }
                }  
            }
        } 
        stage('Run another job') {
            steps {
                echo "${SleepTime}"
                build job: 'Job_With_Parameters-pipeline', 
                    parameters: [
                        [$class: 'StringParameterValue', name: 'String', value: 'Run another job: wait false']
                    ], 
                    wait: false

                catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') { 
                        script {
                            RunBuild1 = build job: 'Job_With_Parameters-pipeline', 
                            parameters:[
                                string(name: 'String', value: 'testing' ),
                                string(name: 'SleepTime', value: "${SleepTime}")
                                ///[$class: 'StringParameterValue', name: 'String', value: 'Run another job: wait true']                              
                            ],
                            wait: true 
                            //echo "${RunBuild1.toString()}"
                        }
               }
            }
        }                
        stage('Run another job1') {
            steps {
                echo "${SleepTime1}"
                build job: 'Job_With_Parameters-pipeline', 
                    parameters: [
                        [$class: 'StringParameterValue', name: 'String', value: 'Run another job: wait false']
                    ], 
                    wait: false

                build job: 'Job_With_Parameters-pipeline', 
                    parameters:[
                        string(name: 'String', value: 'testing:wait true' ),
                        string(name: 'SleepTime', value: "${SleepTime1}")
                        ///[$class: 'StringParameterValue', name: 'String', value: 'Run another job: wait true']                              
                    ],
                    wait: true
            }
        }          
    }
}
