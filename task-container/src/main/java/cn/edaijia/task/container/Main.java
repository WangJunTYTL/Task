package cn.edaijia.task.container;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * this is a simple test for start
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */

public class Main {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("MySystem", ConfigFactory.load());
        TaskContainer.setSystem(system);
        TaskContainer.start();
    }

}


