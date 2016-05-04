package com.peaceful.task.context.config;


import com.peaceful.common.util.chain.BaseChain;

/**
 * 各模块配置信息解析处理链
 * <p/>
 * Created by wangjun on 16/1/12.
 */
public class ConfigParseChain extends BaseChain {

    private ConfigParseChain() {

    }

    public static ConfigParseChain getSingleInstance() {
        return Single.chain;
    }

    public final static class Single {
        public  static ConfigParseChain chain = new ConfigParseChain();
    }

    static {
        Single.chain.addCommand(new LoadConfigFileParse());
        Single.chain.addCommand(new BasicConfigParse());
        Single.chain.addCommand(new TaskDispatchConfigParse());
        Single.chain.addCommand(new ExecutorConfigParse());
    }


}
