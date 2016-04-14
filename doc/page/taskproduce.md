任务生产
---------------

Task是一个轻量级的任务分布式计算系统，它突出的轻量级特点之一就是可以让开发者以最快、最简洁的方式编写自己的分布式方法。如果你在review这样的代码时稍不留心
也许这个方法就是一个分布式地方法......

## 获取对象代理

生产任务的方式已在概述中列出了一个例子，你只需要向下面这样获得Hello【Hello对象只是一个例子】对象的代理即可

    Hello hello = Task.registerASyncClass(Hello.class);

此时Hello对象下的方法就变成了分布式的方法。原理上它是获得对象的代理，把方法的调用描述成一个可以在集群中的任意一个节点在被回放执行且可以暂时存取到MQ组件中的一种调用描述。
由coding模块负责实现这种描述和回放，在回放过程，可以把存取到的MQ任务转成一个Runnable对象交给dispatch模块，最终由Executor执行，具体的可以参照TaskCoding接口的实现。


## Task注解

在生产任务时默认情况下，每个方法被描述成任务送入到MQ的队列位置是对象的名字和方法名，比如概述的say方法，生产任务的队列是`Hello.say`.但有时也许你不想这样，那么Task注解
就可以帮组你更灵活的控制

    @Task("myQueue",executor="myExecutor")
    public void say(String msg) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(msg);
    }

上面的意思是说say方法将被送入到MQ的队列名是myQueue。最终是被名为myExecutor的Executor执行，就是这么简单，你也不可以指定队列名和executor的名字，默认队列、executor的名字分别是default和default。








