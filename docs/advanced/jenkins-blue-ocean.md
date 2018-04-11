<!-- TOC -->

- [Jenkins Blue Ocean](#jenkins-blue-ocean)
        - [先决条件](#%E5%85%88%E5%86%B3%E6%9D%A1%E4%BB%B6)
        - [在 Docker 中运行 jenkins](#%E5%9C%A8-docker-%E4%B8%AD%E8%BF%90%E8%A1%8C-jenkins)
            - [在 mac 和 linux 中运行 jenkins](#%E5%9C%A8-mac-%E5%92%8C-linux-%E4%B8%AD%E8%BF%90%E8%A1%8C-jenkins)
            - [在 windows 中运行 jenkins](#%E5%9C%A8-windows-%E4%B8%AD%E8%BF%90%E8%A1%8C-jenkins)
            - [访问 Jenkins Blue Ocean Docker 容器](#%E8%AE%BF%E9%97%AE-jenkins-blue-ocean-docker-%E5%AE%B9%E5%99%A8)
            - [设置 wizard](#%E8%AE%BE%E7%BD%AE-wizard)
                - [解锁 jenkins](#%E8%A7%A3%E9%94%81-jenkins)
                - [使用插件定制化 jenkins](#%E4%BD%BF%E7%94%A8%E6%8F%92%E4%BB%B6%E5%AE%9A%E5%88%B6%E5%8C%96-jenkins)
                - [创建第一个管理员](#%E5%88%9B%E5%BB%BA%E7%AC%AC%E4%B8%80%E4%B8%AA%E7%AE%A1%E7%90%86%E5%91%98)
            - [启动和终止 jenkins](#%E5%90%AF%E5%8A%A8%E5%92%8C%E7%BB%88%E6%AD%A2-jenkins)
        - [在 github 上 folk 实例仓库](#%E5%9C%A8-github-%E4%B8%8A-folk-%E5%AE%9E%E4%BE%8B%E4%BB%93%E5%BA%93)
        - [在 Blue Ocean 创建 Pipeline 项目](#%E5%9C%A8-blue-ocean-%E5%88%9B%E5%BB%BA-pipeline-%E9%A1%B9%E7%9B%AE)
        - [创建初始化的 Pipeline](#%E5%88%9B%E5%BB%BA%E5%88%9D%E5%A7%8B%E5%8C%96%E7%9A%84-pipeline)
        - [在Pipeline中添加一个test阶段](#%E5%9C%A8pipeline%E4%B8%AD%E6%B7%BB%E5%8A%A0%E4%B8%80%E4%B8%AAtest%E9%98%B6%E6%AE%B5)
        - [在Pipeline中添加一个最终提交阶段](#%E5%9C%A8pipeline%E4%B8%AD%E6%B7%BB%E5%8A%A0%E4%B8%80%E4%B8%AA%E6%9C%80%E7%BB%88%E6%8F%90%E4%BA%A4%E9%98%B6%E6%AE%B5)
        - [跟进（可选的）](#%E8%B7%9F%E8%BF%9B%EF%BC%88%E5%8F%AF%E9%80%89%E7%9A%84%EF%BC%89)
        - [包装](#%E5%8C%85%E8%A3%85)

<!-- /TOC -->

# Jenkins Blue Ocean

This tutorial shows you how to use the [Blue Ocean](https://jenkins.io/doc/book/blueocean/) feature of Jenkins to create a Pipeline that will orchestrate building a simple application.

Before starting this tutorial, it is recommended that you run through at least one of the initial set of tutorials from the [Tutorials overview](https://jenkins.io/doc/tutorials/) page first to familiarize yourself with CI/CD concepts (relevant to a technology stack you’re most familiar with) and how these concepts are implemented in Jenkins.

This tutorial uses the same application that the [Build a Node.js and React app with npm](https://jenkins.io/doc/tutorials/build-a-node-js-and-react-app-with-npm) tutorial is based on. Therefore, you’ll be building the same application although this time, completely through Blue Ocean. Since Blue Ocean provides a simplified Git-handling experience, you’ll be interacting directly with the repository on GitHub (as opposed to a local clone of this repository).

**Duration:** This tutorial takes 20-40 minutes to complete (assuming you’ve already met the [prerequisites](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#prerequisites) below). The exact duration will depend on the speed of your machine and whether or not you’ve already [run Jenkins in Docker](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#run-jenkins-in-docker)from [another tutorial](https://jenkins.io/doc/tutorials/).

You can stop this tutorial at any point in time and continue from where you left off.

If you’ve already run though [another tutorial](https://jenkins.io/doc/tutorials/), you can skip the [Prerequisites](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#prerequisites) and [Run Jenkins in Docker](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#run-jenkins-in-docker) sections below and proceed on to [forking the sample repository](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#fork-sample-repository). If you need to restart Jenkins, simply follow the restart instructions in [Stopping and restarting Jenkins](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#stopping-and-restarting-jenkins) and then proceed on.

### 先决条件

For this tutorial, you will require:

*   A macOS, Linux or Windows machine with:

    *   256 MB of RAM, although more than 512MB is recommended.

    *   10 GB of drive space for Jenkins and your Docker images and containers.

*   The following software installed:

    *   [Docker](https://www.docker.com/) - Read more about installing Docker in the [Installing Docker](https://jenkins.io/doc/book/installing/#installing-docker) section of the [Installing Jenkins](https://jenkins.io/doc/book/installing/) page.
        **Note:** If you use Linux, this tutorial assumes that you are not running Docker commands as the root user, but instead with a single user account that also has access to the other tools used throughout this tutorial.

### 在 Docker 中运行 jenkins

In this tutorial, you’ll be running Jenkins as a Docker container from the [`jenkinsci/blueocean`](https://hub.docker.com/r/jenkinsci/blueocean/) Docker image.

To run Jenkins in Docker, follow the relevant instructions below for either [macOS and Linux](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#on-macos-and-linux) or [Windows](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#on-windows).

You can read more about Docker container and image concepts in the [Docker](https://jenkins.io/doc/book/installing#docker) and [Downloading and running Jenkins in Docker](https://jenkins.io/doc/book/installing#downloading-and-running-jenkins-in-docker)sections of the [Installing Jenkins](https://jenkins.io/doc/book/installing) page.

#### 在 mac 和 linux 中运行 jenkins

1.  Open up a terminal window.

2.  Run the `jenkinsci/blueocean` image as a container in Docker using the following [`docker run`](https://docs.docker.com/engine/reference/commandline/run/) command (bearing in mind that this command automatically downloads the image if this hasn’t been done):

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

    |  | Maps the `/var/jenkins_home` directory in the container to the Docker [volume](https://docs.docker.com/engine/admin/volumes/volumes/) with the name `jenkins-data`. If this volume does not exist, then this `docker run` command will automatically create the volume for you. |
    |  | Maps the `$HOME` directory on the host (i.e. your local) machine (usually the `/Users/<your-username>` directory) to the `/home` directory in the container. |

    **Note:** If copying and pasting the command snippet above doesn’t work, try copying and pasting this annotation-free version here:

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

3.  Proceed to the [Setup wizard](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#setup-wizard).

#### 在 windows 中运行 jenkins

1.  Open up a command prompt window.

2.  Run the `jenkinsci/blueocean` image as a container in Docker using the following [`docker run`](https://docs.docker.com/engine/reference/commandline/run/) command (bearing in mind that this command automatically downloads the image if this hasn’t been done):

    <pre class="nowrap" style="box-sizing: inherit; font-family: Menlo, Monaco, Consolas, &quot;Liberation Mono&quot;, &quot;Courier New&quot;, monospace; font-size: 12.6px; margin-top: 0px; margin-bottom: 1rem; overflow: auto; display: block; color: rgb(41, 43, 44);">docker run ^
      --rm ^
      -u root ^
      -p 8080:8080 ^
      -v jenkins-data:/var/jenkins_home ^
      -v /var/run/docker.sock:/var/run/docker.sock ^
      -v "%HOMEPATH%":/home ^
      jenkinsci/blueocean</pre>

    For an explanation of these options, refer to the [macOS and Linux](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#on-macos-and-linux) instructions above.

3.  Proceed to the [Setup wizard](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#setup-wizard).

#### 访问 Jenkins Blue Ocean Docker 容器

If you have some experience with Docker and you wish or need to access the Jenkins/Blue Ocean Docker container through a terminal/command prompt using the [`docker exec`](https://docs.docker.com/engine/reference/commandline/exec/) command, you can add an option like `--name jenkins-tutorials` (with the[`docker run`](https://docs.docker.com/engine/reference/commandline/run/) above), which would give the Jenkins/Blue Ocean Docker container the name "jenkins-tutorials".

This means you could access the Jenkins/Blue Ocean container (through a separate terminal/command prompt window) with a `docker exec` command like:

`docker exec -it jenkins-tutorials bash`

#### 设置 wizard

Before you can access Jenkins, there are a few quick "one-off" steps you’ll need to perform.

##### 解锁 jenkins

When you first access a new Jenkins instance, you are asked to unlock it using an automatically-generated password.

1.  After the 2 sets of asterisks appear in the terminal/command prompt window, browse to `http://localhost:8080` and wait until the **Unlock Jenkins** page appears.

    ![Unlock Jenkins page](http://upload-images.jianshu.io/upload_images/3101171-54749a3202bbac6c..jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

2.  From your terminal/command prompt window again, copy the automatically-generated alphanumeric password (between the 2 sets of asterisks).

    ![Copying initial admin password](http://upload-images.jianshu.io/upload_images/3101171-5186f869d82486d8..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

3.  On the **Unlock Jenkins** page, paste this password into the **Administrator password** field and click **Continue**.

##### 使用插件定制化 jenkins

After [unlocking Jenkins](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#unlocking-jenkins), the **Customize Jenkins** page appears.

On this page, click **Install suggested plugins**.

The setup wizard shows the progression of Jenkins being configured and the suggested plugins being installed. This process may take a few minutes.

##### 创建第一个管理员

Finally, Jenkins asks you to create your first administrator user.

1.  When the **Create First Admin User** page appears, specify your details in the respective fields and click **Save and Finish**.

2.  When the **Jenkins is ready** page appears, click **Start using Jenkins**.
    **Notes:**

    *   This page may indicate **Jenkins is almost ready!** instead and if so, click **Restart**.

    *   If the page doesn’t automatically refresh after a minute, use your web browser to refresh the page manually.

3.  If required, log in to Jenkins with the credentials of the user you just created and you’re ready to start using Jenkins!

#### 启动和终止 jenkins

Throughout the remainder of this tutorial, you can stop the Jenkins/Blue Ocean Docker container by typing `Ctrl-C` in the terminal/command prompt window from which you ran the `docker run ...` command [above](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#run-jenkins-in-docker).

To restart the Jenkins/Blue Ocean Docker container:

1.  Run the same `docker run ...` command you ran for [macOS, Linux](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#on-macos-and-linux) or [Windows](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#on-windows) above.
    **Note:** This process also updates the `jenkinsci/blueocean` Docker image, if an updated one is available.

2.  Browse to `http://localhost:8080`.

3.  Wait until the log in page appears and log in.

### 在 github 上 folk 实例仓库

Fork the simple "Welcome to React" Node.js and React application on GitHub into your own GitHub account.

1.  Ensure you are signed in to your GitHub account. If you don’t yet have a GitHub account, sign up for a free one on the [GitHub website](https://github.com/).

2.  Fork the [`creating-a-pipeline-in-blue-ocean`](https://github.com/jenkins-docs/creating-a-pipeline-in-blue-ocean) on GitHub into your local GitHub account. If you need help with this process, refer to the [Fork A Repo](https://help.github.com/articles/fork-a-repo/) documentation on the GitHub website for more information.
    **Note:** This is a different repository to the one used in the [Build a Node.js and React app with npm](https://jenkins.io/doc/tutorials/build-a-node-js-and-react-app-with-npm) tutorial. Although these repositories contain the same application code, ensure you fork and use the correct one before continuing on.

### 在 Blue Ocean 创建 Pipeline 项目

1.  Go back to Jenkins and ensure you have accessed the Blue Ocean interface. To do this, make sure you:

    *   have browsed to `http://localhost:8080/blue` and are logged in
        or

    *   have browsed to `http://localhost:8080/`, are logged in and have clicked **Open Blue Ocean** on the left.

2.  In the **Welcome to Jenkins** box at the center of the Blue Ocean interface, click **Create a new Pipeline** to begin the Pipeline creation wizard.
    **Note:** If you don’t see this box, click **New Pipeline** at the top right.

3.  In **Where do you store your code?**, click **GitHub**.

4.  In **Connect to GitHub**, click **Create an access key here**. This opens GitHub in a new browser tab.
    **Note:** If you previously configured Blue Ocean to connect to GitHub using a personal access token, then Blue Ocean takes you directly to step 9 [below](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#choose-github-account).

5.  In the new tab, sign in to your GitHub account (if necessary) and on the GitHub **New Personal Access Token** page, specify a brief **Token description** for your GitHub access token (e.g. `Blue Ocean`).
    **Note:** An access token is usually an alphanumeric string that respresents your GitHub account along with permissions to access various GitHub features and areas through your GitHub account. This access token will have the appropriate permissions pre-selected, which Blue Ocean requires to access and interact with your GitHub account.

6.  Scroll down to the end of the page (leaving all other **Select scopes** options with their default settings) and click **Generate token**.

7.  On the resulting **Personal access tokens** page, copy your newly generated access token.

8.  Back in Blue Ocean, paste the access token into the **Your GitHub access token** field and click **Connect**.

    ![Connecting to GitHub](http://upload-images.jianshu.io/upload_images/3101171-458863b0bff41109..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 
    Jenkins now has access to your GitHub account (provided by your access token).

9.  In **Which organization does the repository belong to?**, click your GitHub account (where you forked the repository [above](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#fork-sample-repository)).

10.  In **Choose a repository**, click your forked repository **creating-a-pipeline-in-blue-ocean**.

11.  Click **Create Pipeline**.
    Blue Ocean detects that there is no Jenkinsfile at the root level of the repository’s `master` branch and proceed to help you create one. (Therefore, you’ll need to click another **Create Pipeline** at the end of the page to proceed.)
    **Note:** Under the hood, a Pipeline project created through Blue Ocean is actually "multibranch Pipeline". Therefore, Jenkins looks for the presence of at least one Jenkinsfile in any branch of your repository.

### 创建初始化的 Pipeline
1.  Following on from creating your Pipeline project ([above](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#create-your-pipeline-project-in-blue-ocean)), in the Pipeline editor, select **docker** from the **Agent** dropdown in the **Pipeline Settings** panel on the right.

    ![Initial to GitHub](http://upload-images.jianshu.io/upload_images/3101171-38172a83441c70fa..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

2.  In the **Image** and **Args** fields that appear, specify `node:6-alpine` and `-p 3000:3000` respectively.

    ![Configuring the agent](http://upload-images.jianshu.io/upload_images/3101171-b8eddc4dff758792..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 
    **Note:** For an explanation of these values, refer to annotations **1** and **2** of the Declarative Pipeline in the [“Create your initial Pipeline…​” section of the Build a Node.js and React app](https://jenkins.io/doc/tutorials/build-a-node-js-and-react-app-with-npm/#create-your-initial-pipeline-as-a-jenkinsfile) tutorial.

3.  Back in the main Pipeline editor, click the **+** icon, which opens the new stage panel on the right.

    ![Add <em>Build</em> stage](http://upload-images.jianshu.io/upload_images/3101171-11407375570182d6..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

4.  In this panel, type `Build` in the **Name your stage** field and then click the **Add Step** button below, which opens the **Choose step type** panel.

    ![Adding the Build stage](http://upload-images.jianshu.io/upload_images/3101171-2bd4e9d04ec6ad8a..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

5.  In this panel, click **Shell Script** near the top of the list (to choose that step type), which opens the **Build / Shell Script** panel, where you can enter this step’s values.

    ![Choosing a step type](http://upload-images.jianshu.io/upload_images/3101171-4313bd83e937a19a..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 
    **Tip:** The most commonly used step types appear closest to the top of this list. To find other steps further down this list, you can filter this list using the **Find steps by name** option.

6.  In the **Build / Shell Script** panel, specify `npm install`.

    ![Specifying a shell step value](http://upload-images.jianshu.io/upload_images/3101171-5f13b34035cb9309..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 
    **Note:** For an explanation of this step, refer to annotation **4** of the Declarative Pipeline in the [“Create your initial Pipeline…​” section of the Build a Node.js and React app](https://jenkins.io/doc/tutorials/build-a-node-js-and-react-app-with-npm/#create-your-initial-pipeline-as-a-jenkinsfile) tutorial.

7.  ( *Optional* ) Click the top-left back arrow icon ![Return from step icon](http://upload-images.jianshu.io/upload_images/3101171-7d8f38c8903f1c36..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  to return to the main Pipeline editor.

8.  Click the **Save** button at the top right to begin saving your new Pipeline with its "Build" stage.

9.  In the **Save Pipeline** dialog box, specify the commit message in the **Description** field (e.g. `Add initial Pipeline (Jenkinsfile)`).

    ![Save Pipeline dialog box](http://upload-images.jianshu.io/upload_images/3101171-d1276ef14325fee7..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

10.  Leaving all other options as is, click **Save & run** and Jenkins proceeds to build your Pipeline.

11.  When the main Blue Ocean interface appears, click the row to see Jenkins build your Pipeline project.
    **Note:** You may need to wait several minutes for this first run to complete. During this time, Jenkins does the following:

    1.  Commits your Pipeline as a `Jenkinsfile` to the only branch (i.e. `master`) of your repository.

    2.  Initially queues the project to be built on the agent.

    3.  Downloads the Node Docker image and runs it in a container on Docker.

    4.  Executes the `Build` stage (defined in the `Jenkinsfile`) on the Node container. (During this time, `npm` downloads many dependencies necessary to run your Node.js and React application, which will ultimately be stored in the local `node_modules` directory within the Jenkins home directory).

        ![Downloading <em>npm</em> dependencies](http://upload-images.jianshu.io/upload_images/3101171-c2ec87636755e2a5..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

    The Blue Ocean interface turns green if Jenkins built your application successfully.

    ![Initial Pipeline runs successfully](http://upload-images.jianshu.io/upload_images/3101171-b3183dcb08a72bfc..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

12.  Click the **X** at the top-right to return to the main Blue Ocean interface.

    ![Main Blue Ocean interface](http://upload-images.jianshu.io/upload_images/3101171-4edff8cbcee96c2f..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 
    **Note:** Before continuing on, you can check that Jenkins has created a `Jenkinsfile` for you at the root of your forked GitHub repository (in the repository’s sole `master` branch).

### 在Pipeline中添加一个test阶段

1.  From the main Blue Ocean interface, click **Branches** at the top-right to access your respository’s branches page, where you can access the `master` branch.

    ![Repository branches page](http://upload-images.jianshu.io/upload_images/3101171-4de79b064cbcc2c8..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

2.  Click the `master` branch’s "Edit Pipeline" icon ![Edit Pipeline on branch](http://upload-images.jianshu.io/upload_images/3101171-6fd492dc10092be8..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  to open the Pipeline editor for this branch.

3.  In the main Pipeline editor, click the **+** icon to the right of the **Build** stage you created [above](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#create-your-initial-pipeline) to open the new stage panel on the right.

    ![Add <em>Test</em> stage](http://upload-images.jianshu.io/upload_images/3101171-0d9949fcdc853465..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

4.  In this panel, type `Test` in the **Name your stage** field and then click the **Add Step** button below to open the **Choose step type**panel.

5.  In this panel, click **Shell Script** near the top of the list.

6.  In the resulting **Test / Shell Script** panel, specify `./jenkins/scripts/test.sh` and then click the top-left back arrow icon ![Return from step icon](http://upload-images.jianshu.io/upload_images/3101171-d454675ef152ba9d..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) to return to the Pipeline stage editor.

7.  At the lower-right of the panel, click **Settings** to reveal this section of the panel.

8.  Click the **+** icon at the right of the **Environment** heading (for which you’ll configure an environment directive).

9.  In the **Name** and **Value** fields that appear, specify `CI` and `true`, respectively.

    ![Environment directive](http://upload-images.jianshu.io/upload_images/3101171-28a5fcdcd8b53be6..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 
    **Note:** For an explanation of this directive and its step, refer to annotations **1** and **3** of the Declarative Pipeline in the [“Add a test stage…​” section of the Build a Node.js and React app](https://jenkins.io/doc/tutorials/build-a-node-js-and-react-app-with-npm/#add-a-test-stage-to-your-pipeline) tutorial.

10.  ( *Optional* ) Click the top-left back arrow icon ![Return from step icon](http://upload-images.jianshu.io/upload_images/3101171-0b56a6a7b0090516..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  to return to the main Pipeline editor.

11.  Click the **Save** button at the top right to begin saving your Pipeline with with its new "Test" stage.

12.  In the **Save Pipeline** dialog box, specify the commit message in the **Description** field (e.g. `Add 'Test' stage`).

13.  Leaving all other options as is, click **Save & run** and Jenkins proceeds to build your amended Pipeline.

14.  When the main Blue Ocean interface appears, click the *top* row to see Jenkins build your Pipeline project.
    **Note:** You’ll notice from this run that Jenkins no longer needs to download the Node Docker image. Instead, Jenkins only needs to run a new container from the Node image downloaded previously. Therefore, running your Pipeline this subsequent time should be much faster.
    If your amended Pipeline ran successfully, here’s what the Blue Ocean interface should look like. Notice the additional "Test" stage. You can click on the previous "Build" stage circle to access the output from that stage.

    ![Test stage runs successfully (with output)](http://upload-images.jianshu.io/upload_images/3101171-ff76652c23b75426..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

15.  Click the **X** at the top-right to return to the main Blue Ocean interface.

### 在Pipeline中添加一个最终提交阶段

1.  From the main Blue Ocean interface, click **Branches** at the top-right to access your respository’s `master` branch.

2.  Click the `master` branch’s "Edit Pipeline" icon ![Edit Pipeline on branch](http://upload-images.jianshu.io/upload_images/3101171-d42f33a3f2e956d7..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  to open the Pipeline editor for this branch.

3.  In the main Pipeline editor, click the **+** icon to the right of the **Test** stage you created [above](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#add-a-test-stage-to-your-pipeline) to open the new stage panel.

    ![Add <em>Deliver</em> stage](http://upload-images.jianshu.io/upload_images/3101171-99125fdd78223103..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

4.  In this panel, type `Deliver` in the **Name your stage** field and then click the **Add Step** button below to open the **Choose step type** panel.

5.  In this panel, click **Shell Script** near the top of the list.

6.  In the resulting **Deliver / Shell Script** panel, specify `./jenkins/scripts/deliver.sh` and then click the top-left back arrow icon ![Return from step icon](http://upload-images.jianshu.io/upload_images/3101171-5304a2d7a591c20d..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  to return to the Pipeline stage editor.

    ![Add next step](http://upload-images.jianshu.io/upload_images/3101171-ec1e84742ec4cf5c..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 
    **Note:** For an explanation of this step, refer to the `deliver.sh` file itself located in the `jenkins/scripts` of your forked repository on GitHub.

7.  Click the **Add Step** button again.

8.  In the **Choose step type** panel, type `input` into the **Find steps by name** field.

    ![Choosing the input step type](http://upload-images.jianshu.io/upload_images/3101171-5ae25fc1d8856734..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

9.  Click the filtered **Wait for interactive input** step type.

10.  In the resulting **Deliver / Wait for interactive input** panel, specify `Finished using the web site? (Click "Proceed" to continue)` in the **Message** field and then click the top-left back arrow icon ![Return from step icon](http://upload-images.jianshu.io/upload_images/3101171-9f30847ca0481100..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  to return to the Pipeline stage editor.

    ![Specifying input step message value](http://upload-images.jianshu.io/upload_images/3101171-cf46e12b30e72015..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 
    **Note:** For an explanation of this step, refer to annotation **4** of the Declarative Pipeline in the [“Add a final deliver stage…​” section of the Build a Node.js and React app](https://jenkins.io/doc/tutorials/build-a-node-js-and-react-app-with-npm/#add-a-final-deliver-stage-to-your-pipeline) tutorial.

11.  Click the **Add Step** button (last time).

12.  Click **Shell Script** near the top of the list.

13.  In the resulting **Deliver / Shell Script** panel, specify `./jenkins/scripts/kill.sh`.
    **Note:** For an explanation of this step, refer to the `kill.sh` file itself located in the `jenkins/scripts` of your forked repository on GitHub.

14.  ( *Optional* ) Click the top-left back arrow icon ![Return from step icon](http://upload-images.jianshu.io/upload_images/3101171-ec14db2ec0c6f49f..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  to return to the main Pipeline editor.

15.  Click the **Save** button at the top right to begin saving your Pipeline with with its new "Deliver" stage.

16.  In the **Save Pipeline** dialog box, specify the commit message in the **Description** field (e.g. `Add 'Deliver' stage`).

17.  Leaving all other options as is, click **Save & run** and Jenkins proceeds to build your amended Pipeline.

18.  When the main Blue Ocean interface appears, click the *top* row to see Jenkins build your Pipeline project.
    If your amended Pipeline ran successfully, here’s what the Blue Ocean interface should look like. Notice the additional "Deliver" stage. Click on the previous "Test" and "Build" stage circles to access the outputs from those stages.

    ![Deliver stage pauses for user input](http://upload-images.jianshu.io/upload_images/3101171-218a0726d8716b1e..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

19.  Ensure you are viewing the "Deliver" stage (click it if necessary), then click the green **`./jenkins/scripts/deliver.sh`** step to expand its content and scroll down until you see the `http://localhost:3000` link.

    ![Deliver stage output only](http://upload-images.jianshu.io/upload_images/3101171-805a1cbf37b946e4..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

20.  Click the `http://localhost:3000` link to view your Node.js and React application running (in development mode) in a new web browser tab. You should see a page/site with the title **Welcome to React** on it.

21.  When you are finished viewing the page/site, click the **Proceed** button to complete the Pipeline’s execution.

    ![Deliver stage runs successfully](http://upload-images.jianshu.io/upload_images/3101171-d10ea243f6f08746..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

22.  Click the **X** at the top-right to return to the main Blue Ocean interface, which lists your previous Pipeline runs in reverse chronological order.

    ![Main Blue Ocean interface with all previous runs displayed](http://upload-images.jianshu.io/upload_images/3101171-786af5a55477454c..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

### 跟进（可选的）

If you check the contents of the `Jenkinsfile` that Blue Ocean created at the root of your forked `creating-a-pipeline-in-blue-ocean` repository, notice the location of the [`environment`](https://jenkins.io/doc/book/pipeline/syntax#environment) directive. This directive’s location within the "Test" stage means that the environment variable `CI` (with its value of `true`) is only available within the scope of this "Test" stage.

You can set this directive in Blue Ocean so that its environment variable is available globally throughout Pipeline (as is the case in the[Build a Node.js and React app with npm](https://jenkins.io/doc/tutorials/build-a-node-js-and-react-app-with-npm/) tutorial). To do this:

1.  From the main Blue Ocean interface, click **Branches** at the top-right to access your respository’s `master` branch.

2.  Click the `master` branch’s "Edit Pipeline" icon ![Edit Pipeline on branch](http://upload-images.jianshu.io/upload_images/3101171-897f588c968ccbff..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  to open the Pipeline editor for this branch.

3.  In the main Pipeline editor, click the **Test** stage you created [above](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#add-a-test-stage-to-your-pipeline) to begin editing it.

4.  In the stage panel on the right, click **Settings** to reveal this section of the panel.

5.  Click the minus (**-**) icon at the right of the `CI` environment directive (you created earlier) to delete it.

6.  Click the top-left back arrow icon ![Return from step icon](http://upload-images.jianshu.io/upload_images/3101171-c063d8aab6a87080..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  to return to the main Pipeline editor.

7.  In the **Pipeline Settings** panel, click the **+** icon at the right of the **Environment** heading (for which you’ll configure a *global*environment directive).

8.  In the **Name** and **Value** fields that appear, specify `CI` and `true`, respectively.

9.  Click the **Save** button at the top right to begin saving your Pipeline with with its relocated environment directive.

10.  In the **Save Pipeline** dialog box, specify the commit message in the **Description** field (e.g. `Make environment directive global`).

11.  Leaving all other options as is, click **Save & run** and Jenkins proceeds to build your amended Pipeline.

12.  When the main Blue Ocean interface appears, click the *top* row to see Jenkins build your Pipeline project.
    You should see the same build process you saw when you completed adding the final deliver stage ([above](https://jenkins.io/doc/tutorials/create-a-pipeline-in-blue-ocean/#add-a-final-deliver-stage-to-your-pipeline)). However, when you inspect the `Jenkinsfile` again, you’ll notice that the `environment` directive is now a sibling of the `agent` section.

### 包装

Well done! You’ve just used the Blue Ocean feature of Jenkins to build a simple Node.js and React application with npm!

The "Build", "Test" and "Deliver" stages you created above are the basis for building other applications in Jenkins with any technology stack, including more complex applications and ones that combine multiple technology stacks together.

Because Jenkins is extremely extensible, it can be modified and configured to handle practically any aspect of build orchestration and automation.

To learn more about what Jenkins can do, check out:

*   The [Tutorials overview](https://jenkins.io/doc/tutorials) page for other introductory tutorials.

*   The [User Handbook](https://jenkins.io/doc/book) for more detailed information about using Jenkins, such as [Pipelines](https://jenkins.io/doc/book/pipeline) (in particular [Pipeline syntax](https://jenkins.io/doc/book/pipeline/syntax)) and the [Blue Ocean](https://jenkins.io/doc/book/blueocean) interface.

*   The [Jenkins blog](https://jenkins.io/node) for the latest events, other tutorials and updates.
