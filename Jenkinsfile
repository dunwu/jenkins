pipeline {
    agent any
    stages {
        stage('构建') {
            steps {
                sh '''
                    cd codes
                    mvn clean package -Dmaven.test.skip=true
                '''
            }
        }
        stage('交付') {
            steps {
                sh '''
                    mkdir -p /home/zp/app/
                    cp codes/target/spring-boot-sample-helloworld.jar /home/zp/app/
                '''
            }
        }
    }
}