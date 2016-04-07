package com.peaceful.task.coding;

import com.peaceful.task.context.helper.IdGenerate;
import org.junit.Test;
import org.slf4j.helpers.Util;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/9
 */
public class IdGenerateTest {

    @Test
    public void testGetNext() throws Exception {
        Util.report(IdGenerate.getNext());
    }
}