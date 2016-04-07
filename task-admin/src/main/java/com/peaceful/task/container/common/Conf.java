package com.peaceful.task.container.common;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * container cluster list conf and web front-end conf
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/24
 * @since 1.6
 */

public class Conf {

    public List<String> clusterList = new ArrayList<String>();
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final ScheduledExecutorService SCHEDULE = Executors.newScheduledThreadPool(1);

    private Conf() {
        SCHEDULE.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Config config = ConfigFactory.load("taskCluster");
                // cluster list
                List<String> list = config.getStringList("taskCluster.clusterList");
                clusterList = list;
            }
        },0,5, TimeUnit.SECONDS);


    }

    private static class Single {
        public static Conf conf = new Conf();
    }

    public static Conf getConf() {
        return Single.conf;
    }


}
