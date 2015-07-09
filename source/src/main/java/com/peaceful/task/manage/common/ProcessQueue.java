package com.peaceful.task.manage.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 请注意参数类型只可以为Map
 *
 * @author wangjun
 * @version 1.0
 * @since 15/5/4.
 */

public class ProcessQueue {

    Logger logger = LoggerFactory.getLogger(getClass());

    //todo test
    public void test(Map params) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("hello world ");
    }

    //todo test
    public void test2(Map params) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info(String.valueOf(params.get("msg")));
    }
}
