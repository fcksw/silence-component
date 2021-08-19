package com.silence.mvc.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {



    @Bean
    public Queue queue() {
        Queue myqueue = new Queue("order-status-queue"); return myqueue;
    }


    @Bean
    public Exchange orderDirectExchange() {

        //durable: 保证exchange数据不丢失
        return new DirectExchange("order-direct-exchange", false, false, null);
    }


    @Bean
    public Binding bindingExchangeQueue() {



        return new Binding(
                "order-status-queue",
                    Binding.DestinationType.QUEUE,
                "order-direct-exchange",
                "order.024.confirm.success",
                null
                );
    }




}
