package com.peaceful.task.kernal.coding.decoding.impl;

import com.peaceful.common.util.chain.Context;
import com.peaceful.task.kernal.coding.decoding.InvokeContext;
import com.peaceful.task.kernal.coding.decoding.NotSupportParamType;
import com.peaceful.task.kernal.coding.decoding.Parse;

/**
 * 这里会验证所有不支持的参数类型，包括
 * <p/>
 * char(Character)
 * short(Short)
 * byte(Byte)
 * float(Float)
 * <p/>
 * 如果遇到这些参数类型，将会抛出 NotSupportParamType()
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/28
 * @since 1.6
 */

public class EmptyParse implements Parse {

    @Override
    public boolean execute(Context context) throws Exception {
        InvokeContext invokeContext = (InvokeContext) context;
        int index = invokeContext.index;
        boolean flag = false;
        Class zClass = invokeContext.parameterTypes[index];
        if (zClass.isPrimitive()) {
            if (zClass.equals(char.class)) {
                flag = true;
            } else if (zClass.equals(byte.class)) {
                flag = true;
            } else if (zClass.equals(float.class)) {
                flag = true;
            } else if (zClass.equals(short.class)) {
                flag = true;
            }
        } else {
            if (zClass.equals(Character.class)) {
                flag = true;
            } else if (zClass.equals(Byte.class)) {
                flag = true;
            } else if (zClass.equals(Float.class)) {
                flag = true;
            } else if (zClass.equals(Short.class)) {
                flag = true;
            }
        }

        if (flag) {
            invokeContext.newArgs[index] = null;
            invokeContext.index++;
            throw new NotSupportParamType(zClass);
        }
        return CONTINUE_PROCESSING;
    }
}
