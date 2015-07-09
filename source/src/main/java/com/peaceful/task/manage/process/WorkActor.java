package com.peaceful.task.manage.process;

import akka.actor.UntypedActor;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import com.peaceful.task.manage.common.TaskManageConf;
import com.peaceful.task.manage.msg.Coordination;
import com.peaceful.task.manage.msg.Task;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.util.Map;

/**
 * Created by wangjun on 15/3/11.
 */
public class WorkActor extends UntypedActor {

    final DiagnosticLoggingAdapter log = Logging.getLogger(this);
    Object processQueue;
    FastClass fastClass;
    Class<?> aClass;

    {
        aClass = TaskManageConf.getConf().aClass;
        fastClass = FastClass.create(aClass);
        processQueue = TaskManageConf.getConf().processQueueInstance;
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof Task) {
            log.debug(getSelf().path().toSerializationFormat() + " receive task");
            Task task = (Task) o;
            log.info("start  " + task.toString());
            FastMethod fastMethod;
            try {
                if ((null == task.getParams())) {
                    fastMethod = fastClass.getMethod(aClass.getMethod(task.getMethod()));
                    fastMethod.invoke(processQueue, new Object[]{0});
                } else {
                    fastMethod = fastClass.getMethod(aClass.getMethod(task.getMethod(), Map.class));
                    fastMethod.invoke(processQueue, new Object[]{task.getParams()});
                }
            } catch (Exception e) {
                log.error(e,"exe task error id is " + task.getId() + " cause is  " + e.getCause() + " error is \n" + e);
            }
//            log.debug(getSender().path().toSerializationFormat());
            log.info("complete  task id is " + task.getId());
            getSender().tell(new Coordination(task.id, task.getQueueName()), getSelf()); // tell sender , it see message
        } else {
            unhandled(o);
        }
    }

}
