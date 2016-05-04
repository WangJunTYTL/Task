package com.peaceful.task.executor.dispatch;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import com.peaceful.task.context.coding.TUR;
import com.peaceful.task.context.dispatch.TaskUnit;
import com.peaceful.task.context.helper.NetHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created by wangjun on 16/1/19.
 */
public class OutsideDispatch {

    Logger logger = LoggerFactory.getLogger(getClass());
    String hostname = NetHelper.getHostname();

    public void dispatch(ActorSelection actorSelection, Collection<TaskUnit> tasks) {
        if (tasks != null) {
            for (TaskUnit q : tasks) {
                TUR unit = TaskUtils.next(q.name);
                if (unit != null)
                    actorSelection.tell(unit, ActorRef.noSender());
            }
        }
    }
}
