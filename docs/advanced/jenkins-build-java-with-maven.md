# 使用 Maven 构建 Java app

本文将向您展示在 Jenkins 中，如何使用 [Maven](https://maven.apache.org/) 构建一个简单的 Java 应用。

## 在 Docker 中运行 Jenkins

在本教程中，您将通过 [jenkinsci/blueocean](https://hub.docker.com/r/jenkinsci/blueocean/) Docker 镜像，将 Jenkins 作为 Docker 容器运行。

为了在 Docker 中运行 Jenkins，请遵循 macOS 和 Linux 或 Windows 的相关说明。

你可以在 [Installing Jenkins](https://jenkins.io/doc/book/installing) 一文的 [Docker](https://jenkins.io/doc/book/installing#docker) 和 [Downloading and running Jenkins in Docker](https://jenkins.io/doc/book/installing#downloading-and-running-jenkins-in-docker) 章节阅读更多关于 Docker 容器和镜像的概念。

### On macOS and Linux

打开终端窗口。

在 Docker 中，使用以下命令行将 `jenkinsci/blueocean` 镜像作为容器运行：

```sh
docker run \
  --rm \
  -u root \
  -p 8080:8080 \
  -v jenkins-data:/var/jenkins_home \ 
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v "$HOME":/home \ 
  jenkinsci/blueocean
```

-----

This tutorial shows you how to use Jenkins to orchestrate building a simple Java application with [Maven](https://maven.apache.org/).

If you are a Java developer who uses Maven and who is new to CI/CD concepts, or you might be familiar with these concepts but don’t know how to implement building your application using Jenkins, then this tutorial is for you.

The simple Java application (which you’ll obtain from a sample repository on GitHub) outputs the string "Hello world!" and is accompanied by a couple of unit tests to check that the main application works as expected. The results of these tests are saved to a JUnit XML report.

**Duration:** This tutorial takes 20-40 minutes to complete (assuming you’ve already met the [prerequisites](https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/#prerequisites) below). The exact duration will depend on the speed of your machine and whether or not you’ve already [run Jenkins in Docker](https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/#run-jenkins-in-docker)from [another tutorial](https://jenkins.io/doc/tutorials/).

You can stop this tutorial at any point in time and continue from where you left off.

If you’ve already run though [another tutorial](https://jenkins.io/doc/tutorials/), you can skip the [Prerequisites](https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/#prerequisites) and [Run Jenkins in Docker](https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/#run-jenkins-in-docker) sections below and proceed on to [forking the sample repository](https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/#fork-sample-repository). (Just ensure you have [Git](https://git-scm.com/downloads) installed locally.) If you need to restart Jenkins, simply follow the restart instructions in [Stopping and restarting Jenkins](https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/#stopping-and-restarting-jenkins) and then proceed on.

### 先决条件

For this tutorial, you will require:

*   A macOS, Linux or Windows machine with:

    *   256 MB of RAM, although more than 512MB is recommended.

    *   10 GB of drive space for Jenkins and your Docker images and containers.

*   The following software installed:

    *   [Docker](https://www.docker.com/) - Read more about installing Docker in the [Installing Docker](https://jenkins.io/doc/book/installing/#installing-docker) section of the [Installing Jenkins](https://jenkins.io/doc/book/installing/) page.
        **Note:** If you use Linux, this tutorial assumes that you are not running Docker commands as the root user, but instead with a single user account that also has access to the other tools used throughout this tutorial.

    *   [Git](https://git-scm.com/downloads) and optionally [GitHub Desktop](https://desktop.github.com/).

### 在 Docker 中运行 Jenkins

In this tutorial, you’ll be running Jenkins as a Docker container from the [`jenkinsci/blueocean`](https://hub.docker.com/r/jenkinsci/blueocean/) Docker image.

To run Jenkins in Docker, follow the relevant instructions below for either [macOS and Linux](https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/#on-macos-and-linux) or [Windows](https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/#on-windows).

You can read more about Docker container and image concepts in the [Docker](https://jenkins.io/doc/book/installing#docker) and [Downloading and running Jenkins in Docker](https://jenkins.io/doc/book/installing#downloading-and-running-jenkins-in-docker)sections of the [Installing Jenkins](https://jenkins.io/doc/book/installing) page.

#### mac 和 linux

1. 打开终端窗口。

2. 使用以下 docker run 命令将 `jenkinsci/blueocean` 镜像作为一个容器运行。

```
docker run \
  --rm \
  -u root \
  -p 8080:8080 \
  -v jenkins-data:/var/jenkins_home \ 
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v "$HOME":/home \ 
  jenkinsci/blueocean
```

将容器中的 `/var/jenkins_home` 目录映射到名为 `jenkins-data` 的目录。如果这个目录不存在，那么这个 `docker run` 命令会自动为你创建目录。

将主机（即本地）计算机上的 `$HOME` 目录（通常是 `/Users/<your-username>` 目录）映射到容器中的 `/home` 目录。

3. 接下来是安装向导。

#### windows

1. 打开终端窗口。

2. 使用以下 docker run 命令将 `jenkinsci/blueocean` 镜像作为一个容器运行。

```
docker run ^
  --rm ^
  -u root ^
  -p 8080:8080 ^
  -v jenkins-data:/var/jenkins_home ^
  -v /var/run/docker.sock:/var/run/docker.sock ^
  -v "%HOMEPATH%":/home ^
  jenkinsci/blueocean
```

3. 接下来是安装向导。

#### 访问 Jenkins / Blue Ocean Docker 容器

如果您对 Docker 有一些经验，并且您希望或需要使用 `docker exec` 命令通过终端/命令提示符访问 Jenkins/Blue Ocean Docker 容器，则可以添加一个选项，如 `--name jenkins-tutorials`（使用docker run上面），这将使 Jenkins/Blue Ocean Docker 容器的名称为“jenkins-tutorials”。

这意味着您可以使用 `docker exec` 命令访问 Jenkins/Blue Ocean Docker 容器（通过单独的终端/命令提示符窗口）

```
docker exec -it jenkins-tutorials bash
```

#### 安装向导

在你访问 Jenkins 之前，需要先执行几个快速的一次性步骤。

##### 解锁 Jenkins

第一次访问 Jenkins 实例时，需要使用自动生成的密码解锁 jenkins。

1. 在终端/命令提示符窗口中出现2组星号后，浏览至http：// localhost：8080并等待，直至出现解锁Jenkins页面。

![Unlock Jenkins page](http://upload-images.jianshu.io/upload_images/3101171-05de61bb8b1a6889..jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2. 再次从终端/命令提示符窗口中，复制自动生成的字母数字密码（在两组星号之间）。

![Copying initial admin password](http://upload-images.jianshu.io/upload_images/3101171-67a46dbda685e79a..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

3. 在解锁 Jenkins 页面上，将此密码粘贴到管理员密码字段中，然后单击继续。

##### 定制插件

解锁 jenkins 后，会出现定制 jenkins 页面。

在这个页面，点击 **Install suggested plugins**

设置向导显示了正在配置的Jenkins的进度以及正在安装的推荐插件。这个过程可能需要几分钟的时间。

##### 创建管理员

最后，jenkins 会提醒你创建一个管理员。

1. 当 **Create First Admin User** 页面出现，填写相关输入框并点击 **Save and Finish**。

2. 当 **Jenkins is ready** 页面出现，点击 **Start using Jenkins**。

    **注意**：

    * 这个页面可能提示 **Jenkins is almost ready!**，这种情况下，点击 **Restart**。
    * 如果这个页面在一分钟后不自动刷新，请在浏览器中手动刷新此页面。

3. 如果需要，请使用刚刚创建的用户的凭据登录到 Jenkins，然后准备好开始使用 Jenkins！

#### 启动终止 jenkins

在本教程的其余部分中，您可以通过在上面运行 `docker run ...` 命令的终端/命令提示符窗口中键入`Ctrl-C` 来停止 Jenkins/Blue Ocean Docker 容器。

重新启动 Jenkins/Blue Ocean Docker 容器：

* 运行您为上面的macOS，Linux或Windows运行的相同 `docker run ...` 命令。
  * 注意：如果更新的可用，此过程还会更新 `jenkinsci/blueocean` Docker 镜像。
* 访问 `http://localhost:8080`。
* 等到登录页面出现并登录。

### 创建 Pipeline 为一个 Jenkinsfile

* 在代码仓库根目录下创建一个 Jenkinsfile 文件，内容为：

```
pipeline {
    agent {
        docker {
            image 'maven:3-alpine' 
            args '-v /root/.m2:/root/.m2' 
        }
    }
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
    }
}
```

点击 **Open Blue Ocean** 

1.  Using your favorite text editor or IDE, create and save new text file with the name `Jenkinsfile` at the root of your local`simple-java-maven-app` Git repository.

2.  Copy the following Declarative Pipeline code and paste it into your empty `Jenkinsfile`:

    ```
    pipeline {
        agent {
            docker {
                image 'maven:3-alpine' 
                args '-v /root/.m2:/root/.m2' 
            }
        }
        stages {
            stage('Build') { 
                steps {
                    sh 'mvn -B -DskipTests clean package' 
                }
            }
        }
    }
    ```

    |  | This `image` parameter (of the [`agent`](https://jenkins.io/doc/book/pipeline/syntax#agent) section’s `docker` parameter) downloads the [`maven:3-apline` Docker image](https://hub.docker.com/_/maven/) (if it’s not already available on your machine) and runs this image as a separate container. This means that:

    *   You’ll have separate Jenkins and Maven containers running locally in Docker.

    *   The Maven container becomes the [agent](https://jenkins.io/doc/book/glossary/#agent) that Jenkins uses to run your Pipeline project. However, this container is short-lived - its lifespan is only that of the duration of your Pipeline’s execution.

     |
    |  | This `args` parameter creates a reciprocal mapping between the `/root/.m2` (i.e. Maven repository) directories in the short-lived Maven Docker container and that of your Docker host’s filesystem. Explaining the details behind this is beyond the scope of this tutorial. However, the main reason for doing this is to ensure that the artifacts necessary to build your Java application (which Maven downloads while your Pipeline is being executed) are retained in the Maven repository beyond the lifespan of the Maven container. This prevents Maven from having to download the same artifacts during successive runs of your Jenkins Pipeline, which you’ll be conducting later on. Be aware that unlike the Docker data volume you created for `jenkins-data` [above](https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/#download-and-run-jenkins-in-docker), the Docker host’s filesystem is effectively cleared out each time Docker is restarted. This means you’ll lose the downloaded Maven repository artifacts each time Docker restarts. |
    |  | Defines a [`stage`](https://jenkins.io/doc/book/pipeline/syntax/#stage) (directive) called `Build` that appears on the Jenkins UI. |
    |  | This [`sh`](https://jenkins.io/doc/pipeline/steps/workflow-durable-task-step/#code-sh-code-shell-script) step (of the [`steps`](https://jenkins.io/doc/book/pipeline/syntax/#steps) section) runs the Maven command to cleanly build your Java application (without running any tests). |

3.  Save your edited `Jenkinsfile` and commit it to your local `simple-java-maven-app` Git repository. E.g. Within the `simple-java-maven-app` directory, run the commands:
    `git add .`
    then
    `git commit -m "Add initial Jenkinsfile"`

4.  Go back to Jenkins again, log in again if necessary and click **Open Blue Ocean** on the left to access Jenkins’s Blue Ocean interface.

5.  In the **This job has not been run** message box, click **Run**, then quickly click the **OPEN** link which appears briefly at the lower-right to see Jenkins running your Pipeline project. If you weren’t able to click the **OPEN** link, click the row on the main Blue Ocean interface to access this feature.
    **Note:** You may need to wait several minutes for this first run to complete. After making a clone of your local `simple-java-maven-app` Git repository itself, Jenkins:

    1.  Initially queues the project to be run on the agent.

    2.  Downloads the Maven Docker image and runs it in a container on Docker.

        ![Downloading Maven Docker image](http://upload-images.jianshu.io/upload_images/3101171-03674153badf6f6b..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

    3.  Runs the `Build` stage (defined in the `Jenkinsfile`) on the Maven container. During this time, Maven downloads many artifacts necessary to build your Java application, which will ultimately be stored in Jenkins’s local Maven repository (in the Docker host’s filesystem).

        ![Downloading Maven artifacts](http://upload-images.jianshu.io/upload_images/3101171-4209b36c5d6a24aa..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

    The Blue Ocean interface turns green if Jenkins built your Java application successfully.

    ![Initial Pipeline runs successfully](http://upload-images.jianshu.io/upload_images/3101171-732ad61f62b81ce8..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

6.  Click the **X** at the top-right to return to the main Blue Ocean interface.

    ![Main Blue Ocean interface](http://upload-images.jianshu.io/upload_images/3101171-5a54dcf359be742a..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

### 添加一个 test 阶段

1.  Go back to your text editor/IDE and ensure your `Jenkinsfile` is open.

2.  Copy and paste the following Declarative Pipeline syntax immediately under the `Build` stage of your `Jenkinsfile`:

    ```
            stage('Test') {
                steps {
                    sh 'mvn test'
                }
                post {
                    always {
                        junit 'target/surefire-reports/*.xml'
                    }
                }
            }
    ```

    so that you end up with:

    ```
    pipeline {
        agent {
            docker {
                image 'maven:3-alpine'
                args '-v /root/.m2:/root/.m2'
            }
        }
        stages {
            stage('Build') {
                steps {
                    sh 'mvn -B -DskipTests clean package'
                }
            }
            stage('Test') { 
                steps {
                    sh 'mvn test' 
                }
                post {
                    always {
                        junit 'target/surefire-reports/*.xml' 
                    }
                }
            }
        }
    }
    ```

    |  | Defines a [`stage`](https://jenkins.io/doc/book/pipeline/syntax/#stage) (directive) called `Test` that appears on the Jenkins UI. |
    |  | This [`sh`](https://jenkins.io/doc/pipeline/steps/workflow-durable-task-step/#code-sh-code-shell-script) step (of the [`steps`](https://jenkins.io/doc/book/pipeline/syntax/#steps) section) executes the Maven command to run the unit test on your simple Java application. This command also generates a JUnit XML report, which is saved to the `target/surefire-reports` directory (within the `/var/jenkins_home/workspace/simple-java-maven-app` directory in the Jenkins container). |
    |  | This [`junit`](https://jenkins.io/doc/pipeline/steps/junit/#code-junit-code-archive-junit-formatted-test-results) step (provided by the [JUnit Plugin](https://jenkins.io/doc/pipeline/steps/junit)) archives the JUnit XML report (generated by the `mvn test` command above) and exposes the results through the Jenkins interface. In Blue Ocean, the results are accessible through the**Tests** page of a Pipeline run. The [`post`](https://jenkins.io/doc/book/pipeline/syntax/#post) section’s `always` condition that contains this `junit` step ensures that the step is *always* executed *at the completion* of the `Test` stage, regardless of the stage’s outcome. |

3.  Save your edited `Jenkinsfile` and commit it to your local `simple-java-maven-app` Git repository. E.g. Within the `simple-java-maven-app` directory, run the commands:
    `git stage .`
    then
    `git commit -m "Add 'Test' stage"`

4.  Go back to Jenkins again, log in again if necessary and ensure you’ve accessed Jenkins’s Blue Ocean interface.

5.  Click **Run** at the top left, then quickly click the **OPEN** link which appears briefly at the lower-right to see Jenkins running your amended Pipeline project. If you weren’t able to click the **OPEN** link, click the *top* row on the Blue Ocean interface to access this feature.
    **Note:** You’ll notice from this run that Jenkins no longer needs to download the Maven Docker image. Instead, Jenkins only needs to run a new container from the Maven image downloaded previously. Also, if Docker had not restarted since you last ran the Pipeline [above](https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/#create-your-initial-pipeline-as-a-jenkinsfile), then no Maven artifacts need to be downloaded during the "Build" stage. Therefore, running your Pipeline this subsequent time should be much faster.
    If your amended Pipeline ran successfully, here’s what the Blue Ocean interface should look like. Notice the additional "Test" stage. You can click on the previous "Build" stage circle to access the output from that stage.

    ![Test stage runs successfully (with output)](http://upload-images.jianshu.io/upload_images/3101171-48790b9848ff8d5a..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

6.  Click the **X** at the top-right to return to the main Blue Ocean interface.

### 添加一个最终交付阶段

1.  Go back to your text editor/IDE and ensure your `Jenkinsfile` is open.

2.  Copy and paste the following Declarative Pipeline syntax immediately under the `Test` stage of your `Jenkinsfile`:

    ```
            stage('Deliver') {
                steps {
                    sh './jenkins/scripts/deliver.sh'
                }
            }
    ```

    so that you end up with:

    ```
    pipeline {
        agent {
            docker {
                image 'maven:3-alpine'
                args '-v /root/.m2:/root/.m2'
            }
        }
        stages {
            stage('Build') {
                steps {
                    sh 'mvn -B -DskipTests clean package'
                }
            }
            stage('Test') {
                steps {
                    sh 'mvn test'
                }
                post {
                    always {
                        junit 'target/surefire-reports/*.xml'
                    }
                }
            }
            stage('Deliver') { 
                steps {
                    sh './jenkins/scripts/deliver.sh' 
                }
            }
        }
    }
    ```

    |  | Defines a new stage called `Deliver` that appears on the Jenkins UI. |
    |  | This [`sh`](https://jenkins.io/doc/pipeline/steps/workflow-durable-task-step/#code-sh-code-shell-script) step (of the [`steps`](https://jenkins.io/doc/book/pipeline/syntax/#steps) section) runs the shell script `deliver.sh` located in the `jenkins/scripts` directory from the root of the `simple-java-maven-app` repository. Explanations about what this script does are covered in the`deliver.sh` file itself. As a general principle, it’s a good idea to keep your Pipeline code (i.e. the `Jenkinsfile`) as tidy as possible and place more complex build steps (particularly for stages consisting of 2 or more steps) into separate shell script files like the `deliver.sh` file. This ultimately makes maintaining your Pipeline code easier, especially if your Pipeline gains more complexity. |

3.  Save your edited `Jenkinsfile` and commit it to your local `simple-java-maven-app` Git repository. E.g. Within the `simple-java-maven-app` directory, run the commands:
    `git stage .`
    then
    `git commit -m "Add 'Deliver' stage"`

4.  Go back to Jenkins again, log in again if necessary and ensure you’ve accessed Jenkins’s Blue Ocean interface.

5.  Click **Run** at the top left, then quickly click the **OPEN** link which appears briefly at the lower-right to see Jenkins running your amended Pipeline project. If you weren’t able to click the **OPEN** link, click the *top* row on the Blue Ocean interface to access this feature.
    If your amended Pipeline ran successfully, here’s what the Blue Ocean interface should look like. Notice the additional "Deliver" stage. Click on the previous "Test" and "Build" stage circles to access the outputs from those stages.

    ![Deliver stage runs successfully](http://upload-images.jianshu.io/upload_images/3101171-267e75bfaedf4082..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

    Here’s what the output of the "Deliver" stage should look like, showing you the execution results of your Java application at the end.

    ![Deliver stage output only](http://upload-images.jianshu.io/upload_images/3101171-e8c3372c4553bd2a..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

6.  Click the **X** at the top-right to return to the main Blue Ocean interface, which lists your previous Pipeline runs in reverse chronological order.

    ![Main Blue Ocean interface with all previous runs displayed](http://upload-images.jianshu.io/upload_images/3101171-83b4dd2ae9cea59f..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

### 结束语

Well done! You’ve just used Jenkins to build a simple Java application with Maven!

The "Build", "Test" and "Deliver" stages you created above are the basis for building more complex Java applications with Maven in Jenkins, as well as Java and Maven applications that integrate with other technology stacks.

Because Jenkins is extremely extensible, it can be modified and configured to handle practically any aspect of build orchestration and automation.

To learn more about what Jenkins can do, check out:

*   The [Tutorials overview](https://jenkins.io/doc/tutorials) page for other introductory tutorials.

*   The [User Handbook](https://jenkins.io/doc/book) for more detailed information about using Jenkins, such as [Pipelines](https://jenkins.io/doc/book/pipeline) (in particular [Pipeline syntax](https://jenkins.io/doc/book/pipeline/syntax)) and the[Blue Ocean](https://jenkins.io/doc/book/blueocean) interface.

*   The [Jenkins blog](https://jenkins.io/node) for the latest events, other tutorials and updates.
