package cn.edaijia.task.container.job;

import cn.edaijia.task.container.msg.Task;
import cn.edaijia.task.container.msg.Task2;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

/**
 * taskContainer 可以识别的任务单元描述形式
 * 1.0版本形式为：{@link Task}
 * 2.0版本形式为：{@link Task2}
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public class TaskProtocol {


    public static Object parse(String str) {
        String version = JSON.parseObject(str).getString("version");
        if (StringUtils.isEmpty(version)) {
            return JSON.parseObject(str, Task.class);
        } else {
            return JSON.parseObject(str, Task2.class);
        }
    }
}
