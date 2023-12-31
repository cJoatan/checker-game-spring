package br.com.codr.realtimechecker.services;

import br.com.codr.realtimechecker.WebSocketSessions;
import br.com.codr.realtimechecker.models.dto.MessageDTO;
import br.com.codr.realtimechecker.models.dto.MessageType;
import br.com.codr.realtimechecker.models.dto.TextMessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Optional;

@Service
public class CheckerGameTopicListener {

    @Value("${checker-game.board.kafka.topic")
    private String topicName;

    private final WebSocketSessions webSocketSessions;

    private final BoardsService boardsService;

    public CheckerGameTopicListener(WebSocketSessions webSocketSessions, BoardsService boardsService) {
        this.webSocketSessions = webSocketSessions;
        this.boardsService = boardsService;
    }

    @KafkaListener(topics = "${checker-game.board.kafka.topic}", groupId = "checker_game_group")
    public void consumeBoard(ConsumerRecord<String, String> payload) throws JsonProcessingException {

        final var objectMapper = new ObjectMapper();
        final var messageDTO = objectMapper.readValue(payload.value(), MessageDTO.class);
        final var byId = boardsService.findById(messageDTO.getId());
        byId.ifPresent(board -> {

            webSocketSessions.sessions.stream()
                .filter(session -> session.getId().equals(board.getPlayerWhite().getSessionId())
                                || session.getId().equals(board.getPlayerBlack().getSessionId()))
                .forEach(session -> {
                    try {
                        session.sendMessage(new TextMessage(payload.value()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        });
    }


    @KafkaListener(topics = "${checker-game.chat.kafka.topic}", groupId = "checker_game_group")
    public void consumeChat(ConsumerRecord<String, String> payload) throws JsonProcessingException {
        System.out.println(payload);
        final var objectMapper = new ObjectMapper();
        final var messageDTO = objectMapper.readValue(payload.value(), MessageDTO.class);
//        boardsService.findById(messageDTO.getId())
//                .ifPresent(board -> {
//
//                    final var textMessageIsAdded = boardsService.addTextMessage(board, currentSession.getId(), messageDTO.getMessageText());
//                    if (textMessageIsAdded.isPresent()) {
//                        final var textMessage = textMessageIsAdded.get();
//
//                        final var textMessageDTO = new TextMessageDTO();
//                        textMessageDTO.setText(messageDTO.getMessageText());
//                        textMessageDTO.setGameUserId(messageDTO.getMessageText());
//                        textMessageDTO.setOrder(textMessage.getOrder());
//                        textMessageDTO.setType(MessageType.SEND_CHAT_MESSAGE);
//
//                        try {
//                            final var response = objectMapper.writeValueAsString(textMessageDTO);
//                            sendMessageToOtherPlayer(response);
//                        } catch (JsonProcessingException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

    }


}
