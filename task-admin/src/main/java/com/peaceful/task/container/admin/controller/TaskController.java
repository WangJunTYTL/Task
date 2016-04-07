package com.peaceful.task.container.admin.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.peaceful.common.util.Http;
import com.peaceful.task.container.task.api.GraphData;
import com.peaceful.task.container.task.api.RedisControllerApi;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author WangJun
 * @version 1.0 16/4/1
 */
@Controller
@RequestMapping("/")
@ResponseBody
public class TaskController {

    @RequestMapping(value = "taskmap", method = RequestMethod.POST)
    @Description("获取所有任务配置")
    public void getTaskMap(HttpServletRequest request) {
        String name = (String) request.getAttribute("currentCluster");
        Http.responseJSON(RedisControllerApi.getTaskDetail(name));
    }

    @RequestMapping(value = "tasknodemap", method = RequestMethod.POST)
    @Description("获取集群节点")
    public void getTaskNodeMap(HttpServletRequest request) {
        String name = (String) request.getAttribute("currentCluster");
        Http.responseJSON(RedisControllerApi.getNodes(name));
    }

    @RequestMapping(value = "tasknodemap/update", method = RequestMethod.POST)
    @Description("更新节点配置")
    public void updateNode(HttpServletRequest request) {
        String name = (String) request.getAttribute("currentCluster");
        String node = (String) request.getParameter("node");
        String method = (String) request.getParameter("method");
        if (method.equals("obtain")) {
            Http.responseJSON(RedisControllerApi.getNodes(name).get(node));
            return;
        } else {
            String state = (String) request.getParameter("state");
            RedisControllerApi.updateNode(name, node, state);
        }
        Http.responseJSON("OK");
    }

    @RequestMapping(value = "taskmap/update", method = RequestMethod.POST)
    @Description("更新任务配置")
    public void updateTask(HttpServletRequest request) {
        String name = (String) request.getAttribute("currentCluster");
        String task = (String) request.getParameter("task");
        String method = (String) request.getParameter("method");
        String desc = (String) request.getParameter("desc");
        if (desc == null || desc.equals("")) desc = "无";
        if (method.equals("obtain")) {
            Http.responseJSON(RedisControllerApi.getTaskDetail(name).get(task));
            return;
        } else {
            String state = (String) request.getParameter("state");
            RedisControllerApi.updateTask(name, task, state, desc);
        }
        Http.responseJSON("OK");
    }

    @RequestMapping(value = "executor", method = RequestMethod.POST)
    @Description("获取executor列表")
    public void getExecutor(HttpServletRequest request) {
        String name = (String) request.getAttribute("currentCluster");
        Http.responseJSON(RedisControllerApi.getExecutors(name));
    }

    @RequestMapping(value = "rate/total", method = RequestMethod.POST)
    @Description("获取任务的生产和消费速率")
    public void getRateGraph(HttpServletRequest request) {
        String name = (String) request.getAttribute("currentCluster");
        GraphData graphData = RedisControllerApi.getCycleCount(name);
        if (graphData != null) {
            JSONObject data = new JSONObject();
            data.put("tag", graphData.tag);
            data.put("timeAxis", graphData.timeAxis.get());
            data.put("produceAxis", graphData.produceAxis.get());
            data.put("consumeAxis", graphData.consumeAxis.get());
            Http.responseJSON(data);
        } else {
            Http.responseJSON("EMPTY");
        }

    }

    @RequestMapping(value = "rate/task", method = RequestMethod.POST)
    @Description("获取每个任务的生产和消费速率")
    public void getTaskRate(HttpServletRequest request) {
        String name = (String) request.getAttribute("currentCluster");
        Map<String, String> produce = RedisControllerApi.getTaskProduceRate(name);
        Map<String, String> consume = RedisControllerApi.getTaskConsumeRate(name);
        Set<String> tasks = new HashSet<String>();
        tasks.addAll(produce.keySet());
        tasks.addAll(consume.keySet());
        JSONArray array = new JSONArray();
        for (String t : tasks) {
            JSONObject data = new JSONObject();
            data.put("task",t);
            if (produce.containsKey(t)) {
                data.put("produce", produce.get(t));
            } else {
                data.put("produce", 0);
            }

            if (consume.containsKey(t)) {
                data.put("consume", consume.get(t));
            } else {
                data.put("consume", 0);
            }
            array.add(data);

        }
        Http.responseJSON(array);
    }


}
