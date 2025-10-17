job('nodejs-basic-job') {
    description('A basic Node.js build job created using Jenkins Job DSL.')

    scm {
        git('https://github.com/mkbedi/jenkins-demo.git', 'main')
    }

    triggers {
        scm('H/5 * * * *')  // Poll every 5 minutes for changes
    }

    wrappers {
        nodejs('NodeJs 25')  // Use Jenkins NodeJS tool (configure this in Jenkins → Global Tool Configuration)
    }

    steps {
        shell('npm install')
    }
}
