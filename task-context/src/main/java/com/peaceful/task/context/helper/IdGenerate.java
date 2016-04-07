package com.peaceful.task.context.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 唯一id生成器
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public class IdGenerate {

    static Logger logger = LoggerFactory.getLogger(IdGenerate.class);

    /**
     * 通过nanoTime作为唯一ID
     *
     * @return
     */
    public static String getNext() {
        String hostName = "null";
        String threadName = "null";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("id generate error {}", e);
        }
        threadName = Thread.currentThread().getName();
        return String.valueOf(hostName + "-" + threadName + "-" + System.nanoTime() / 1000);
    }

    public static String getNext(String prefix) {
        return prefix + "-" + getNext();

    }
}
