package com.silence.mvc.rabbit.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderResultConsumer {

    @RabbitListener(queues = "order-status-queue")
    public void orderStatusConsumer(String message) {
        System.out.println(message);
    }

}
