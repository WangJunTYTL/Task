package com.peaceful.task.system;

/**
 * @author WangJun
 * @version 1.0 16/3/30
 */
public class SetUp {

    public static void main(String[] args) throws InterruptedException {
        Hello hello = Task.registerASyncClass(Hello.class);
//        int time = 1;
        int time = 10000;
        for (int i = 0; i < time; i++) {
            hello.say("hello world");
        }
    }
}
