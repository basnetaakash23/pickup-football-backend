package com.football.pickup.games.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.football.pickup.games.dto.request.EmailRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailEventProducer {

    private final KafkaTemplate<Object, String> kafkaTemplate;
    private static final String TOPIC = "email-topic-2";

    public void sendEmailEvent(EmailRequestDto emailRequest) throws JsonProcessingException {
        kafkaTemplate.send(TOPIC, new ObjectMapper().writeValueAsString(emailRequest));

    }


}
