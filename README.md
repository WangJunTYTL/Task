Task系统设计与使用
----------------

Task是一个轻量级的分布式任务计算系统,他可以帮助你快速编写一个可以在集群环境下运行的分布式方法,而这只需要你使用一行代码就可以在你原有的方法上做到.

一个简单例子:

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
        // 此时say方法会被立即返回且是被集群中的某个节点给调用执行
        hello.say("hello world");
    }
}

````

## Task说明文档

此文档只适合2.6-SNAPSHOT版本

1. [概述](./doc/page/overview.md)
2. [快速上手](./doc/page/quickstart.md)
2. [Task配置](./doc/page/taskconf.md)
2. [监控部署](./doc/page/taskmonitor.md)
2. [任务生产](./doc/page/taskproduce.md)
2. [调度策略](./doc/page/dispatch.md)
2. [可扩展](./doc/page/extension.md)
3. ...待更新


## 交流

QQ群：365133362 群名称：互联网从业者