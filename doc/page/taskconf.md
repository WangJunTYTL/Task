Task配置
---------------

Task在启动时会首先加载默认的配置文件：task-reference.conf，然后再去加载用户自定义的配置文件。
用户自定义配置文件默认路径是classpath下的task.conf文件，也可以在启动时通过-Dtask.conf.file fileName指定用户配置文件路径。

**注意：**
1. 如果用户没有提供自己的配置文件，系统会使用默认的参数配置，即只加载task-reference.conf的配置。
2. 如果用户有自己的配置文件，用户自定义配置和系统配置采用合并的策略，对于同样的配置项用户配置会覆盖系统默认配置

### 默认配置信息如下：

```
task {

  # 系统命名,主要用于创建多个系统实例时的命名空间的区分
  name = "TASK"

  # 启动模式,如果是client模式,只可以提交任务,不会调度任务执行,server模式可提交任务,也可以调度任务执行
  boot-mode = "server"  # server[client]

  # 开发模式，在test模式下，系统会打印更多的日志信息
  develop-mode = "test" # test[product]

  # 任务队列实现，你也可以实现自己的队列通过继承TaskQueue
  queue = "com.peaceful.task.queue.redis.RedisQueue"

  # 任务bean实例获取工厂，任务在被执行时，需要获取任务的实例，你也可以实现自己的bean管理工厂，比如对接Spring的BeanFactory或其它DI容器
  bean-factory = "com.peaceful.task.context.dispatch.TaskBeanFactoryImpl"

  // 任务执行器
  executor = [
    {
      name: "default"
      # 系统默认的executor实现,基于akka的actor并发模型
      implementation: "com.peaceful.task.executor.impl.actor.ActorTaskExecutor"
    },
    {
      name: "simple"
      # 最简单的executor实现
      implementation: "com.peaceful.task.executor.impl.SimpleTaskExecutor"
    },
    {
      name: "jucExecutor"
      implementation: "com.peaceful.task.executor.impl.JUCTaskExecutor"
    }
  ]

  # 从MQ中拉取任务的频率
  dispatch-tick = 2s
}
```
