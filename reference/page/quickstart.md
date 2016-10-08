Task快速上手
---------------

这里主要目的是帮助你快速上手,为了没这么多麻烦,你有必要按照以下步骤完整的学习下,如果你正在使用window系统,需要按照build.sh脚本的步骤一步步实践

## 为构建做准备

Task默认采用maven的方式进行依赖管理和构建.所以这之前请确认你已经安装了maven.另外由于在构建过程中会去到github上拉取其它依赖项目,所以在
这之前还有必要安装git

## 开始构建

准备动作完成后,构建过程就很简单了,只需要执行`build.sh`即可,如果构架成功,会在最后提示:恭喜你,构建成功!

## 实践

下面就可以在实际项目中操练了.你需要新建一个项目,或是在你的实际项目中,引入以下依赖:

````
<dependency>
    <groupId>com.peaceful</groupId>
    <artifactId>task-system</artifactId>
    <!--注意,xx为所使用的Task版本 -->
    <version>XX-SNAPSHOT</version>
</dependency>
````
然后像最开始说的代码样例进行尝试

````

public class Hello {

    public void say(String msg) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(msg);
    }
}


public class SetUp {

    public static void main(String[] args) throws InterruptedException {
        // 获取Hello对象的代理实例
        Hello hello = Task.registerASyncClass(Hello.class);
        // 此时say方法会被立即返回且是被集群中的某个节点给调用了
        hello.say("hello world");
    }
}

````

## 启动Redis服务

在本地启动redis服务.采用默认连接:127.0.0.1:6379

运行上面的SetUp方法并观察结果
