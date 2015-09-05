package com.peaceful.task.container.invoke.args.impl;

import com.peaceful.task.container.invoke.InvokeContext;
import com.peaceful.task.container.invoke.args.Parse;
import com.peaceful.task.container.invoke.chain.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Long类型参数解析
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
            return true;
        }
        return false;
    }
}
