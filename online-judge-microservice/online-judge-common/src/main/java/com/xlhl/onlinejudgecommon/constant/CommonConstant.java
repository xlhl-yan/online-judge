package com.xlhl.onlinejudgecommon.constant;

/**
 * 通用常量
 *
 * @author <a href="https://github.com/xlhl-yan">xlhl</a>
 */
public interface CommonConstant {

    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = " descend";

    /**
     * 交换机名称
     */
    String EXCHANGE_NAME = "oj.exchange";

    /**
     * 队列名称
     */
    String QUEUE_NAME = "oj.queue";

    /**
     * 交互的Routing Key
     */
    String ROUTING_KEY = "judge";
}
