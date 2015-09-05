package com.peaceful.task.container.invoke.excute;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/28
 * @since 1.6
 */

public class NotFoundMethod extends RuntimeException {

    public NotFoundMethod(String methodName, Object[] paramTypes) {
        super("not found method " + methodName + ",paramsTypes is " + paramTypes);
    }
}
