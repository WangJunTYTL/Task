package com.peaceful.task.kernal;

/**
 * 管理Task系统在执行任务时 ,任务对象所属实例获取方式.
 * <p>
 * 如果你的项目使用类似ioc容器管理bean,你也可以自己实现该接口,从你的ioc容器中获取任务所属实例
 *
 * @author WangJun
 * @version 1.0 16/3/31
 */
public interface TaskBeanFactory {

    <T> T getBean(Class<T> zclass);

    void  newInstance(Object instance);

}
