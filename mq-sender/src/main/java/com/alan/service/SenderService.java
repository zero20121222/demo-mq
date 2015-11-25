package com.alan.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2015-11-23.
 */
@Service
public class SenderService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendInfo(String message){
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend("exchange1", "queue0", message);
        rabbitTemplate.convertAndSend("exchange0", "queue1", message+1);
        rabbitTemplate.convertAndSend("exchange0", "queue0", message+2);
    }
}
