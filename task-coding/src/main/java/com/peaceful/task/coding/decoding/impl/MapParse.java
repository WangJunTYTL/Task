package com.peaceful.task.coding.decoding.impl;

import com.peaceful.task.coding.decoding.InvokeContext;
import com.peaceful.task.coding.decoding.Parse;
import com.peaceful.common.util.chain.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Map类型参数解析
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/27
 * @since 1.6
 */

public class MapParse implements Parse {

    @Override
    public boolean execute(Context context) throws Exception {
        InvokeContext invokeContext = (InvokeContext) context;
        if (invokeContext.parameterTypes[invokeContext.index].equals(Map.class)) {
            JSONObject object = (JSONObject) invokeContext.args[invokeContext.index];
            invokeContext.newArgs[invokeContext.index] = JSON.parseObject(object.toJSONString(), invokeContext.parameterTypes[invokeContext.index]);
            invokeContext.index++;
            return PROCESSING_COMPLETE;
        }
        return CONTINUE_PROCESSING;
    }
}
