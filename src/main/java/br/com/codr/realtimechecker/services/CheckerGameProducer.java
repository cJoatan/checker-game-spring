package br.com.codr.realtimechecker.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckerGameProducer {

    @Value("${checker-game.board.kafka.topic}")
    private String boardTopicName;

    @Value("${checker-game.chat.kafka.topic}")
    private String chatTopicName;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendBoard(String message) {
        log.info("Board Payload sended: {}", message);
        kafkaTemplate.send(boardTopicName, message);
    }

    public void sendTextMessage(String message) {
        log.info("Chat Payload sended: {}", message);
        kafkaTemplate.send(chatTopicName, message);
    }

}
