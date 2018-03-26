pipeline {
    agent any
    stages {
        stage('安装库') {
            steps {
                sh '''
                    cd codes
                    npm install
                '''
            }
        }
        stage('构建') {
            steps {
                sh '''
                    cd codes
                    npm run build
                '''
            }
        }
        stage('交付') {
            steps {
                sh '''
                    mkdir -p /home/zp/app/
                    cp -rf codes/build /home/zp/app/react-demo
                '''
            }
        }
    }
}