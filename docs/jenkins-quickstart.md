<!-- TOC -->

- [Jenkins 快速指南](#jenkins-%E5%BF%AB%E9%80%9F%E6%8C%87%E5%8D%97)
    - [概念](#%E6%A6%82%E5%BF%B5)
        - [Pipeline](#pipeline)
        - [Jenkinsfile](#jenkinsfile)
    - [创建 Pipeline](#%E5%88%9B%E5%BB%BA-pipeline)
        - [Jenkinsfile 简单实例](#jenkinsfile-%E7%AE%80%E5%8D%95%E5%AE%9E%E4%BE%8B)
    - [运行多步骤](#%E8%BF%90%E8%A1%8C%E5%A4%9A%E6%AD%A5%E9%AA%A4)
        - [简单实例](#%E7%AE%80%E5%8D%95%E5%AE%9E%E4%BE%8B)
            - [Linux, BSD, and Mac OS](#linux-bsd-and-mac-os)
            - [Windows](#windows)
            - [Timeouts, retries and more](#timeouts-retries-and-more)
            - [整理](#%E6%95%B4%E7%90%86)
    - [定义执行环境](#%E5%AE%9A%E4%B9%89%E6%89%A7%E8%A1%8C%E7%8E%AF%E5%A2%83)
    - [环境变量](#%E7%8E%AF%E5%A2%83%E5%8F%98%E9%87%8F)
    - [记录测试和 artifact](#%E8%AE%B0%E5%BD%95%E6%B5%8B%E8%AF%95%E5%92%8C-artifact)
    - [清空和通知](#%E6%B8%85%E7%A9%BA%E5%92%8C%E9%80%9A%E7%9F%A5)
        - [邮件](#%E9%82%AE%E4%BB%B6)
    - [部署](#%E9%83%A8%E7%BD%B2)
        - [阶段作为部署环境](#%E9%98%B6%E6%AE%B5%E4%BD%9C%E4%B8%BA%E9%83%A8%E7%BD%B2%E7%8E%AF%E5%A2%83)
    - [要求人工输入](#%E8%A6%81%E6%B1%82%E4%BA%BA%E5%B7%A5%E8%BE%93%E5%85%A5)

<!-- /TOC -->

# Jenkins 快速指南

## 概念

### Pipeline

[Pipeline](https://jenkins.io/doc/book/pipeline/) 是一套插件，用来支持在 Jenkins 中实现和集成持续交付通道。

持续交付渠道是您从软件版本控制到用户和客户流程的自动化表达。

Pipeline 提供了一组可扩展的工具，通过 [Pipeline DSL](https://jenkins.io/doc/book/pipeline/syntax) 将“简单到复杂”的交付管道“作为代码”建模。

### Jenkinsfile

Jenkins Pipeline 的定义通常写入一个文本文件，称为 Jenkinsfile，该文件又被检入到项目的源代码控制库中。

## 创建 Pipeline

1. 在代码仓库中创建 `Jenkinsfile`，内容参考 [Jenkinsfile 简单实例](#jenkinsfile-%E7%AE%80%E5%8D%95%E5%AE%9E%E4%BE%8B)。

2. 点击 Jenkins 菜单中的**新建（New Item）**按钮。

3. 输入一个任务名称并选择 **Multibranch Pipeline**

4. 点击**增加源（Add Source）**按钮，选择代码仓库类型。

5. 点击**保存（Save）**按钮，然后观察第一个 Pipeline 运行。

### Jenkinsfile 简单实例

**Java**

```
pipeline {
    agent { docker { image 'maven:3.3.3' } }
    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }
    }
}
```

**Node.js / JavaScript**

```
pipeline {
    agent { docker { image 'node:6.3' } }
    stages {
        stage('build') {
            steps {
                sh 'npm --version'
            }
        }
    }
}
```

**Ruby**

```
pipeline {
    agent { docker { image 'ruby' } }
    stages {
        stage('build') {
            steps {
                sh 'ruby --version'
            }
        }
    }
}
```

**Python**

```
pipeline {
    agent { docker { image 'python:3.5.1' } }
    stages {
        stage('build') {
            steps {
                sh 'python --version'
            }
        }
    }
}
```

**PHP**

```
pipeline {
    agent { docker { image 'php' } }
    stages {
        stage('build') {
            steps {
                sh 'php --version'
            }
        }
    }
}
```

## 运行多步骤

Pipelines 由多个步骤组成，允许您构建、测试和部署应用程序。Jenkins Pipeline 允许您以简单的方式撰写多个步骤，可以帮助您对任何类型的自动化过程建模。

想象一个“步骤”就像执行单个动作的单个命令一样。当一个步骤成功时，它将转到下一步。当一个步骤未能正确执行时，Pipeline 将失败。

当 Pipeline 中的所有步骤都成功完成， Pipeline 就被视作执行成功。

### 简单实例

#### Linux, BSD, and Mac OS

在 Linux，BSD 和 Mac OS（Unix-like）系统中，`sh` 步骤用于在管道中执行 shell 命令。

```
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'echo "Hello World"'
                sh '''
                    echo "Multiline shell steps works too"
                    ls -lah
                '''
            }
        }
    }
}
```

#### Windows

基于 Windows 的系统应该使用 `bat` 步骤来执行批处理命令。

```
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                bat 'set'
            }
        }
    }
}
```

#### Timeouts, retries and more

有一些有用的步骤可以“包装”其他步骤，这些步骤可以轻松解决复杂问题，例如重试（`retry`）步骤直至成功或退出步骤需要很长时间（`timeout`）。

```
pipeline {
    agent any
    stages {
        stage('Deploy') {
            steps {
                retry(3) {
                    sh './flakey-deploy.sh'
                }

                timeout(time: 3, unit: 'MINUTES') {
                    sh './health-check.sh'
                }
            }
        }
    }
}
```

`Deploy` 阶段重试 flakey-deploy.sh 脚本 3 次，然后等待最多 3 分钟执行 health-check.sh 脚本。如果运行状况检查脚本在 3 分钟内未完成，管道将在“部署”阶段被标记为失败。

子步骤（如 `retry` 和 `timeout`）可能包含其他步骤，包括 `retry` 或 `timeout`。

我们可以组合这些步骤。例如，如果我们想重试我们的部署 5 次，但从未想过总共花费超过3分钟，然后才能进入阶段：

```
pipeline {
    agent any
    stages {
        stage('Deploy') {
            steps {
                timeout(time: 3, unit: 'MINUTES') {
                    retry(5) {
                        sh './flakey-deploy.sh'
                    }
                }
            }
        }
    }
}
```

#### 整理

当管道完成执行时，您可能需要运行清理步骤或根据管道的结果执行一些操作。这些操作可以在 post 部分中执行。

```
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'echo "Fail!"; exit 1'
            }
        }
    }
    post {
        always {
            echo 'This will always run'
        }
        success {
            echo 'This will run only if successful'
        }
        failure {
            echo 'This will run only if failed'
        }
        unstable {
            echo 'This will run only if the run was marked as unstable'
        }
        changed {
            echo 'This will run only if the state of the Pipeline has changed'
            echo 'For example, if the Pipeline was previously failing but is now successful'
        }
    }
}
```

## 定义执行环境

在上一节中，您可能已经注意到每个示例中的 agent 指令。agent 指令告诉 Jenkins 在哪里以及如何执行 Pipeline 或其子集。正如您所预料的那样，所有 Pipeline 都需要 `agent`。

在引擎盖下面，代理原因发生了一些事情：

* 块中包含的所有步骤均由 Jenkins 排队等待执行。只要执行者可用，这些步骤就会开始执行。
* 将分配一个工作空间，该工作空间将包含从源代码管理检出的文件以及 Pipeline 的任何其他工作文件。

Pipeline 旨在轻松使用 Docker 镜像和容器在内部运行。这允许 Pipeline 定义所需的环境和工具，而无需手动配置各种系统工具和代理依赖关系。这种方法使您可以使用任何可以打包在 Docker 容器中的工具。

```
pipeline {
    agent {
        docker { image 'node:7-alpine' }
    }
    stages {
        stage('Test') {
            steps {
                sh 'node --version'
            }
        }
    }
}
```

当 Pipeline 执行时，Jenkins 会自动启动指定的容器并执行定义的步骤。

```
[Pipeline] stage
[Pipeline] { (Test)
[Pipeline] sh
[guided-tour] Running shell script
+ node --version
v7.4.0
[Pipeline] }
[Pipeline] // stage
[Pipeline] }
```

## 环境变量

环境变量可以全局设置，如下面的示例或每个阶段。正如您所预料的那样，为每个阶段设置环境变量意味着它们仅适用于定义它们的阶段。

```
pipeline {
    agent any

    environment {
        DISABLE_AUTH = 'true'
        DB_ENGINE    = 'sqlite'
    }

    stages {
        stage('Build') {
            steps {
                sh 'printenv'
            }
        }
    }
}
```

这种在 Jenkins 文件中定义环境变量的方法对于指示脚本（如 `Makefile`）在构建或测试等不同的场景运行非常有用。

环境变量的另一个常见用途是在构建或测试脚本中设置或覆盖“虚拟”证书。由于将证书直接放入Jenkins文件中显然不是一个好主意，因此Jenkins Pipeline允许用户快速安全地访问Jenkins文件中的预定义凭据，而无需了解其值。

## 记录测试和 artifact

虽然测试是一个良好的持续交付管道的关键部分，但大多数人不希望筛选数千行控制台输出来查找有关失败测试的信息。为了简化操作，只要您的测试运行器可以输出测试结果文件，Jenkins就可以记录和汇总测试结果。 Jenkins通常与junit步骤捆绑在一起，但是如果您的测试运行器无法输出JUnit样式的XML报告，那么还有其他插件可以处理任何广泛使用的测试报告格式。

为了收集我们的测试结果和文物，我们将使用 `post` 部分。

```
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh './gradlew check'
            }
        }
    }
    post {
        always {
            junit 'build/reports/**/*.xml'
        }
    }
}
```

这将始终抓取测试结果，让 Jenkins 跟踪他们，计算趋势并报告他们。未通过测试的 Pipeline 将被标记为“UNSTABLE”，在Web UI中用黄色标记。这与“失败”状态不同，用红色标记。

当出现测试失败时，从 Jenkins 抓取构建的 artifcat 以进行本地分析和调查通常很有用。Jenkins 内置的支持存储了artifcat，这是 Pipeline 执行期间生成的文件。

这很容易通过 `archiveArtifacts` 步骤和文件匹配表达式完成，如下例所示：

```
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './gradlew build'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew check'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
            junit 'build/reports/**/*.xml'
        }
    }
}
```

如果在 `archiveArtifacts` 步骤中指定了多个参数，则必须在步骤代码中明确指定每个参数的名称 - 即为 `artifact` 的路径和文件名以及指纹选择此选项的工件。如果您只需指定工件的路径和文件名，则可以省略参数名称`artifact` - 例如
`archiveArtifacts'build / libs / ** / *。jar'`

在 Jenkins 中记录测试和 artifact 有利于快速轻松地向团队中的各个成员提供信息。

## 清空和通知

由于Pipeline的post部分保证在Pipeline执行结束时运行，因此我们可以添加一些通知或其他步骤来执行最终化，通知或其他Pipeline结束任务。

```
pipeline {
    agent any
    stages {
        stage('No-op') {
            steps {
                sh 'ls'
            }
        }
    }
    post {
        always {
            echo 'One way or another, I have finished'
            deleteDir() /* clean up our workspace */
        }
        success {
            echo 'I succeeeded!'
        }
        unstable {
            echo 'I am unstable :/'
        }
        failure {
            echo 'I failed :('
        }
        changed {
            echo 'Things were different before...'
        }
    }
}
```

有很多方法可以发送通知，下面是一些片段，演示如何将有关管道的通知发送到电子邮件，Hipchat聊天室或Slack频道。

### 邮件

```
post {
    failure {
        mail to: 'team@example.com',
             subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
             body: "Something is wrong with ${env.BUILD_URL}"
    }
}
```

## 部署


最基本的连续交付管道至少有三个阶段，这些阶段应该在Jenkins文件中定义：构建，测试和部署。在本节中，我们主要关注部署阶段，但应该指出，稳定的构建和测试阶段是任何部署活动的重要先导。

```
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying'
            }
        }
    }
}
```

### 阶段作为部署环境

一种常见模式是扩展阶段的数量以捕获其他部署环境，如“分段”或“生产”，如以下代码片段所示。

```
stage('Deploy - Staging') {
    steps {
        sh './deploy staging'
        sh './run-smoke-tests'
    }
}
stage('Deploy - Production') {
    steps {
        sh './deploy production'
    }
}
```

在这个例子中，我们假设我们的 `./run-smoke-tests` 脚本运行的任何“smoke tests”足以限定或验证生产环境的发布。这种自动将代码全部部署到生产的流水线可被视为“持续部署”的实现。尽管这是一种崇高的理想，但对于许多人来说，持续部署可能不实际的原因很多，但仍然可以享受持续交付的好处。Jenkins 管道很容易支持两者。

## 要求人工输入

通常在阶段之间，特别是环境阶段之间传递时，您可能需要在继续之前手动输入信息。例如，判断应用程序是否处于足够好的状态以“促进”到生产环境。这可以通过输入步骤来完成。在下面的例子中，“Sanity check”阶段实际上阻止了输入，并且在没有人确认进度的情况下不会进行。

```
pipeline {
    agent any
    stages {
        /* "Build" and "Test" stages omitted */

        stage('Deploy - Staging') {
            steps {
                sh './deploy staging'
                sh './run-smoke-tests'
            }
        }

        stage('Sanity check') {
            steps {
                input "Does the staging environment look ok?"
            }
        }

        stage('Deploy - Production') {
            steps {
                sh './deploy production'
            }
        }
    }
}
```