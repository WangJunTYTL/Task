任务生产
---------------

Task是一个轻量级的任务分布式计算系统，它突出的轻量级特点之一就是可以让开发者以最快、最简洁的方式编写自己的分布式方法。如果你在review这样的代码时稍不留心
也许这个方法就是一个分布式地方法。

## 获取对象代理

生产任务的方式已在概述中列出了一个例子，你只需要向下面这样获得一个对象的代理实例即可，例如

    Hello hello = Task.registerASyncClass(Hello.class);

此时Hello对象下的方法就变成了一个分布式的任务生产方法。原理上它是获得对象的代理实例，把方法的调用描述成一个可以在集群中的任意一个节点在被回放执行且可以暂时存取到MQ组件中的一种调用描述。
由coding模块负责实现这种描述和回放，在回放过程，可以把存取到的MQ任务转成一个Runnable对象交给dispatch模块，最终由Executor执行，具体的调用描述和回放可以参照TaskCoding接口的实现。


## Task注解

在生产任务时默认情况下，每个方法被描述成任务送入到MQ的队列位置是对象的名字和方法名，比如概述中的say方法，生产任务的队列是`Hello.say`.但有时也许你不想这样，那么Task注解
就可以帮组你更灵活的控制

    @Task("myQueue",executor="myExecutor")
    public void say(String msg) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(msg);
    }

上面的意思是说say方法将被送入到MQ的队列名是myQueue。最终是被名为myExecutor的Executor执行，就是这么简单，你也可以不指定队列名和executor的名字，默认queue、executor的名字分别是default和default。

## 方法描述

方法调用被描述为一个任务后被放入MQ服务中。当前默认MQ服务采用的是Redis的List结构作为MQ服务，方法调用描述采用Json的方式，比如概述中的say方法调用描述信息如下

```
"{
    "aclass": "com.peaceful.task.system.Hello",
    "args": [
        "helloworld!"
    ],
    "executor": "default",
    "id": "Test06-wangjunxe7x9ax84MacBook-main-9897997744",
    "method": "say",
    "parameterTypes": [
        "java.lang.String"
    ],
    "queueName": "Test06",
    "submitTime": 1460741276765,
    "version": "2.0"
}"
```

这非常类似于一个RPC的远程调用协议，但还是有些区别，比如任务的场景主要是为了计算，所以不允许作为任务的方法有返回值，方法的参数也收到一些限制，主要支持
含有状态属性的参数，支持如下类型：

1. 支持基本类型即对应的包装类型。但除外基本类型中char、byte、float、short即对应的包装类型默认`不支持`。
3. 支持简单POJO类型支持，且变量属性是public修饰
2. 支持3大集合类型：Set、List、Map，集合内实际类型也受到该约束限制
4. 复杂类型需要用户自定义类型解释器









