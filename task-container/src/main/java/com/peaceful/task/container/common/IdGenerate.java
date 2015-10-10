package com.peaceful.task.container.common;

/**
 * 唯一id生成器
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public class IdGenerate {


    /**
     * 通过nanoTime作为唯一ID
     *
     * @return
     */
    public static String getNext() {
        return String.valueOf(System.nanoTime() / 1000);
    }
}
