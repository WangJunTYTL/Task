package com.peaceful.task.container.console;

/**
 * Task console api
 * <p/>
 * 任务容器对外控制接口
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/16
 * @since 1.6
 */

public interface Console {


    /**
     * 锁住某个任务的调度，但可以继续提交任务
     *
     * @param key 任务队列名称
     * @return 如果被锁住，则返回true，否则 false
     */
    boolean lockTask(String key);

    /**
     * 锁住某个任务的调度，并在指定时间后释放锁但可以继续提交任务
     *
     * @param key 任务队列名称
     * @return 如果被锁住，则返回true，否则 false
     */
    boolean lockTask(String key, int seconds);

    /**
     * 对某个任务的调度解锁，使其可以继续调度
     *
     * @param key 任务队列名称
     * @return 如果锁被成功打开，则返回true，否则 false
     */
    boolean openTask(String key);

    /**
     * 查看某个锁是否被锁
     *
     * @param key 任务队列名称
     * @return 如果被锁则返回true，否则 false
     */
    boolean isLock(String key);


}
