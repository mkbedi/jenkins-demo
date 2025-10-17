pipelineJob('nodejs-jenkins-pipeline-job') {
    description('A pipeline-based Node.js CI/CD job created with Jenkins Job DSL.')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/mkbedi/jenkins-demo.git')
                    }
                    branch('main')
                }
            }
            scriptPath('Jenkinsfile') // Jenkinsfile path inside the repo
        }
    }

    triggers {
        scm('H/5 * * * *')  // Poll every 5 minutes for Git changes
    }

    properties {
        disableConcurrentBuilds()   // Prevent overlapping runs
    }
}
