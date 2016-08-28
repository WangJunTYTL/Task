package com.peaceful.task.core.test;

import com.peaceful.task.core.Task;

import java.util.*;

/**
 * @author WangJun
 * @version 1.0 16/3/30
 */
public class SetUp {

    public static void main(String[] args) throws InterruptedException {

        Task task = Task.create();
        Hello hello = task.registASyncClass(Hello.class);
        int time = 1;
        for (int i=0;i< time;i++) {
            // 基本类型测试
            hello.say("hello world!");

            User user = new User();
            user.name = "123";

            // 简单POJO类型
            hello.testPOJO(user);

            // Map类型
            Map<String, User> userMap = new HashMap<String, User>();
            userMap.put("01", user);
            hello.testMap(userMap);

            // List 类型
            List<User> users = new ArrayList<User>();
            users.add(user);
            hello.testList(users);

            // Set 类型
            Set<User> userSet = new HashSet<User>();
            userSet.add(user);
            hello.testSet(userSet);

            hello.testAll("Hello World!", user, userMap, users, userSet);
        }

//        int time = 1;
     /*   int time = 10000;
        for (int i = 0; i < time; i++) {
            hello.say("hello world");
        }*/
    }
}
