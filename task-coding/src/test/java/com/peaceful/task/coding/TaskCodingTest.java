package com.peaceful.task.coding;

import com.alibaba.fastjson.JSON;
import com.peaceful.task.context.TaskCoding;
import com.peaceful.task.context.coding.TU;
import org.junit.Test;
import org.slf4j.helpers.Util;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/12
 */
public class TaskCodingTest {

    TaskCoding taskCodingI = new TaskCodingImpl();

    @Test
    public void testEncoding() throws Exception {
        TU task02 = new TU();
        task02.aclass = Hello.class;
        task02.args = new Object[]{"hello world"};
        task02.method = "say";
        task02.parameterTypes = new Class[]{java.lang.String.class};
        task02 = taskCodingI.encoding(task02.aclass, task02.aclass.getMethod("say", task02.parameterTypes), task02.args);
        Util.report(task02.toString());
    }

    @Test
    public void testDecoding() throws Exception {

        TU task02 = new TU();
        task02.aclass = Hello.class;
        task02.args = new Object[]{"hello world"};
        task02.method = "say";
        task02.parameterTypes = new Class[]{java.lang.String.class};
        // 编码成一个TU对象
        task02 = taskCodingI.encoding(task02.aclass, task02.aclass.getMethod("say", task02.parameterTypes), task02.args);

        // 解码TU对象为TUR对象
        Runnable runnable = taskCodingI.decoding(JSON.toJSONString(task02));
        if (runnable != null)
            runnable.run();

    }
}