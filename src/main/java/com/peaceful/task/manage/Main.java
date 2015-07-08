package com.peaceful.task.manage;

import akka.actor.ActorSystem;

/**
 * this is a simple test for start
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */

public class Main {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("MySystem");
        QueueServiceStart.setSystem(system);
        QueueServiceStart.run();
    }

}


