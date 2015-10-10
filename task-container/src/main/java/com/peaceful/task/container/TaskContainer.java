package com.peaceful.task.container;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.peaceful.task.container.admin.common.ConfConstant;
import com.peaceful.task.container.admin.common.LoadDependModule;
import com.peaceful.task.container.admin.common.TaskContainerLogger;
import com.peaceful.task.container.console.Monitor;
import com.peaceful.task.container.console.MonitorImpl;
import com.peaceful.task.container.dispatch.TaskSuperviseActor;
import com.peaceful.task.container.schedule.*;
import org.slf4j.Logger;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * 启动任务容器，该类是一个比较重的类，只需启动一次
 * <p/>
 * Created by wangjun on 15/1/22.
 */
public class TaskContainer {

    private static Logger LOGGER = TaskContainerLogger.ROOT_LOGGER;

    /**
     * 本地并发模型采用actor模型，该模型的实现是通过akka框架，有关akka可以访问 http://akka.io
     */
    private static ActorSystem system;


    /**
     * 注入外部actor框架
     * <p/>
     * 可以在启动前调用调用
     *
     * @param system 外部actor框架
     */
    public static void setSystem(ActorSystem system) {
        TaskContainer.system = system;
    }

    public static ActorSystem getSystem() {
        return TaskContainer.system;
    }

    /**
     * 加入监控模块，用于监控整个集群的情况
     */
    public static Monitor monitor = MonitorImpl.getInstance();


    public static void start() {

        if (system == null) {
            system = ActorSystem.create("TASK");
            LOGGER.info("akka system is default!");
        }


        LoadDependModule.loadToJVM();


        /** 任务容器的监管者，负责监管任务调度中心和容器压力、报警监控 */
        ActorRef supervise = system.actorOf(Props.create(TaskSuperviseActor.class), "taskSupervise");

        /**
         * 由于当前是任务容器是主动去任务存储中心拉取任务，所以需要有一个轮训者不断的区任务存储中心拉取任务
         * 当轮训中拉取到任务时，会送到任务路由中心，接下来的时刻会在worker和router之间协调的去任务存储中心拉取任务
         * 第二，为了保证router不会在某一个任务上不断的调度，该轮询方法需要固定频率的给router强加任务，使每个任务队列都有机会调度
         */
        system.scheduler().schedule(Duration.Zero(), Duration.create(ConfConstant.FOCUSED_SCHEDULE_TICK, TimeUnit.SECONDS), new FocusedTaskSupervisorRunnable(supervise), system.dispatcher());

        /**
         * 监控 FlexibleTask
         * 需要轮训的检测任务存储中心动态任务的情况
         * 当发现有新的FlexibleTask出现在注册表中，首先从注册表中清除掉该FlexibleTask，然后申请调度，并添加该Flexible到FirstFlexibleTaskMap，用于监控
         * 为了保证FlexibleTask不会有遗漏
         * ---------------------------
         * 1.首先每次提交FlexibleTask都会去改注册表中注册
         * 2.每个集群节点拿到FlexibleTask需要添加到TaskContainerConf#flexibleTasks
         * 3.每个节点需要保证存储中心的FlexibleTask为0，才会清除在本地的TaskContainerConf#flexibleTasks中
         */

        TaskSchedule.schedule(Duration.Zero(), ConfConstant.FIRST_FLEXIBLE_SCHEDULE_TICK, TimeUnit.SECONDS, new FlexibleTaskSupervisorRunnable(supervise, monitor));


        /**
         * Auto FirstFlexibleTask To SecondFlexibleTask
         * 为了减少服务器对动态任务的检查，所以设置二级FlexibleTask容器，用于将已经完成的动态任务切换到二级容器中
         */
        TaskSchedule.schedule(Duration.Zero(), ConfConstant.FLEXIBLE_FIRST_SECOND_CHECK_TICK, TimeUnit.MINUTES, new FlexibleTaskCircleRunnable(monitor));

        /**
         * 集群监控分析
         */
        TaskSchedule.scheduleOnce(1, TimeUnit.SECONDS, new MonitorRunnable(monitor));

    }

}
