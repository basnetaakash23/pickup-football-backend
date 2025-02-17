package com.football.pickup.games.service.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    private static final String DESTINATION = "chatQueue";

    public void sendMessage(String message) {
        jmsTemplate.convertAndSend(DESTINATION, message);
    }

}
