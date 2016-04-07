package com.peaceful.task.container.task.api;

import java.io.Serializable;

/**
 * 基于数据实现的循环队列，用于存取最新的固定长度的数据
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/16
 * @since 1.6
 */

public class CycleQueue<E> implements Serializable {

    /**
     * 数组下一个索引位置
     */
    private int index;
    /**
     * 循环队列的大小
     */
    private int length;
    /**
     * 是否队列已处于循环转态
     */
    private boolean cycle;
    /**
     * 存取具体数据的容器
     */
    private Object[] data;


    /**
     * 利用指定大小的值初始化循环队列容器
     *
     * @param length
     */
    public CycleQueue(int length) {
        this.length = length;
        data = new Object[length];
    }

    /**
     * 线程安全的存入最新值到容器中
     *
     * @param value
     */
    public synchronized void push(E value) {
        if (index >= length) {
            index = index - length;
            cycle = true;
        }
        data[index] = value;
        index++;
    }

    /**
     * 获取目前容器内的所有值
     *
     * @return
     */
    public Object[] get() {
        Object[] truthData = new Object[length];
        int currentIndex = (index - 1);
        if (cycle) {
            for (int i = (length - 1); i >= 0; i--) {
                if (currentIndex < 0) {
                    truthData[i] = data[currentIndex + length];
                } else {
                    truthData[i] = data[currentIndex];
                }
                currentIndex--;
            }
        } else {
            Object[] data = new Object[index];
            for (int i = 0; i <= currentIndex; i++) {
                data[i] = this.data[i];
            }
            return data;
        }
        return truthData;
    }

    /**
     * 获取容器内的最新值
     *
     * @return
     */
    public E getCurrentValue() {
        int currentIndex = (index - 1);
        if (currentIndex < 0) currentIndex = 0;
        return (E) data[currentIndex];
    }

    @Override
    public String toString() {
        Object[] data = get();
        String res = "[ ";
        for (Object object : data) {
            res += object + ",";
        }
        res = res.substring(0, res.length() - 1);
        res += " ]";
        return res;
    }
}
