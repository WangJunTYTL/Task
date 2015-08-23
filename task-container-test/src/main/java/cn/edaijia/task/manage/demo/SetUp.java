package cn.edaijia.task.manage.demo;


import akka.actor.ActorSystem;
import cn.edaijia.task.container.TaskContainer;
import com.typesafe.config.ConfigFactory;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public class SetUp {

    // 启动任务容器
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("MySystem", ConfigFactory.load("application.conf"));
        TaskContainer.setSystem(system);
        TaskContainer.start();
    }
}
