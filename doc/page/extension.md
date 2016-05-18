可扩展
------------

Task的设计是定位是作为一款分布式任务执行引擎。可以本身单独运行，也可以灵活方便的和其它系统组件配合使用。

### 扩展MQ组件

Task默认的MQ组件实现是基于Redis的List数据结构。具体可参考：com.peaceful.task.queue.redis.RedisQueue。有关Redis的配置使用可参考：https://github.com/WangJunTYTL/redismanage

如果你要对接自己公司内部的MQ组件，需要继承TaskQueue接口：实现如下接口

```
public interface TaskQueue<T> {


    boolean push(String name, T object);

    T pop(String name);

    long size(String key);
}
```

另外需要在配置文件中设置: queue = "MQ实现类全名"

### 扩展BeanFactory

Task在被执行时，需要获取任务所属的实例，Task默认的Bean管理方式是通过反射获取实例并缓存起来。如果你想自己管理Bean实例或某些bean实例被DI容器管理再或者某些复杂的bean需要自己手动实例，你可以实现
TaskBeanFactory接口：

```
public interface TaskBeanFactory {


    <T> T getBean(Class<T> zclass);

    // 对于复杂的bean手动实例化并添加到BeanFactory
    void  newInstance(Object instance);

}

```
另外需要在配置文件中设置: bean-factory = "BeanFactory实现类全名"

### 扩展Executor

Task在执行时会被回放成一个Runnable对象交给Executor执行，Task是建议用户管理自己Executor，这样可以方便用户隔离不同的任务，可以支持任务的优先级、防止任务阻塞的特性。Task默认只是给了3个样例，它们的特点是：

1. com.peaceful.task.executor.impl.actor.ActorTaskExecutor: 交由Dispatch模块管理运行任务
2. com.peaceful.task.executor.impl.SimpleTaskExecutor: 直接执行任务
3. com.peaceful.task.executor.impl.JUCTaskExecutor: 利用JUC包下的Executors框架实现

在一个Task系统下可以支持同时启动多个Executor，有关配置可以参照task-reference.conf配置文件





