package com.peaceful.task.system;

import com.peaceful.task.context.annotation.Task;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/11
 */
public class Hello {

    @Task(value = "Test06"/*,executor = "jucExecutor"*/)
    public void say(String msg) throws InterruptedException {
//        Thread.sleep(1000);
        System.out.println(msg);
    }

    @Task("Test08")
    public void testPOJO(User user) throws InterruptedException {
        System.out.println(user.name);
    }

    public void testMap(Map<String, User> user) throws InterruptedException {
        System.out.println(user.get("01").name);
    }

    public void testList(List<User> user) throws InterruptedException {
        System.out.println(user.get(0).name);
    }

    public void testSet(Set<User> user) throws InterruptedException {
        System.out.println(user);
    }

    public void testAll(String msg,User user,Map<String, User> userMap,List<User> userList,Set<User> userSet) throws InterruptedException {
        System.out.println(msg);
        System.out.println(userMap.get("01").name);
        System.out.println(userList.get(0).name);
        System.out.println(user);
    }
}
