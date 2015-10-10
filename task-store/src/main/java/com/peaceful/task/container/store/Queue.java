package com.peaceful.task.container.store;

/**
 * 任务存储模块，使用队列的方式存储任务单元
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public interface Queue {

    /**
     * 存入任务单元
     *
     * @param key   队列名
     * @param value 任务单元
     */
    void push(String key, String value);

    /**
     * 取出一个任务单元
     *
     * @param key 队列名
     * @return 返回任务单元，加入不存在返回 null
     */
    String pop(String key);

    /**
     * 获取当前任务队列中任务单元的个数
     *
     * @param key 队列名
     * @return 返回任务单元的个数
     */
    long size(String key);

    /**
     * 锁住当前任务队列的调度
     *
     * @param key 队列名
     * @return 返回加锁是否成功
     */
    boolean lock(String key);

    /**
     * 锁住当前任务队列的调度,并在指定时间后自动释放锁
     *
     * @param key 队列名
     * @return 返回加锁是否成功
     */
    boolean lock(String key, int seconds);

    /**
     * 指定队列是否加锁
     *
     * @param key 队列名
     * @return 如果加锁返回 true 否则返回false
     */
    boolean isLock(String key);

    /**
     * 对指定队列解锁
     *
     * @param key 队列名
     * @return 返回是否解锁成功
     */
    boolean openLock(String key);

}
