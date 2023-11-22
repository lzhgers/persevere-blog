package com.lzh.lzhframework.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luzhiheng
 * @date 2023-11-06
 */
@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.queue}")
    private String queue;

    @Value("${rabbitmq.routingKey}")
    private String routingKey;

    @Bean
    public TopicExchange testTopicExchange() {
        return new TopicExchange(exchange, false, false);
    }

    @Bean
    public Queue testQueue() {
        // 自动删除 - 队列没有对应的消费者 会自动删除
        return new Queue(queue);
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(testQueue()).to(testTopicExchange()).with(routingKey);
    }
}
