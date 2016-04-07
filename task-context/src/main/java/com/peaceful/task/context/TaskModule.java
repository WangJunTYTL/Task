package com.peaceful.task.context;

import com.peaceful.common.util.chain.Command;
import com.peaceful.common.util.chain.Context;

/**
 * task module 的开发都需要继承该接口,按照该接口规范的编写的模块才允许被Task系统启动时加载
 *
 * @author WangJun
 * @version 1.0 16/3/29
 */
public interface TaskModule extends Command {


}
