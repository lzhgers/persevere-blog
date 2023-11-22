package com.lzh.blog.admin.ampq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author  luzhiheng
 * @date 2023/11/6 17:49
 */
@Component
@RabbitListener(queues = "testQueue10")
public class TestConsumer {

    @RabbitHandler
    public void received(String msg) {
        try {
            System.out.println("消费者执行：" + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
