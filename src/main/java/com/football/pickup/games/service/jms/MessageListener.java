package com.football.pickup.games.service.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @JmsListener(destination = "chatQueue")
    public void receiveMessage(String message) {
        System.out.println("Received Message: " + message);
    }
}
