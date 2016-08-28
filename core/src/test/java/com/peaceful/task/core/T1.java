package com.peaceful.task.core;

import com.peaceful.common.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/4/2
 */
public class T1 {

    public static void main(String[] args) {
        Map<String ,String> a = new HashMap<String, String>();
        Map<String ,String> b = new HashMap<String, String>();
        a.put("1","1");
        a.put("1","2");
        b.put("1","1");
        a.putAll(b);
        Util.report(a);
        Util.report(TimeUnit.MINUTES.toMillis(5));

    }
}
