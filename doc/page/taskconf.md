Task配置
---------------

Task在启动时默认加载的配置文件路径是classpath下的task.conf文件，配置也比较简单，如下

```
task {

  # 系统命名,主要用于创建多个系统实例时的命名空间的区分
  name = "demo"

  // 启动模式,如果是client模式,只可以提交任务,不会调度任务执行,server模式可提交任务,也可以调度任务执行
  boot-mode = "server"

  develop-mode = "test"

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
