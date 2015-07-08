package cn.edaijia.queue.exception;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/20
 * @since 1.6
 */

public class QueueServiceConfigException extends RuntimeException {

    public QueueServiceConfigException(String msg){
        super(msg);
    }
    public QueueServiceConfigException(Throwable e){
        super(e);
    }
}
