package br.com.codr.realtimechecker;

import br.com.codr.realtimechecker.models.dto.BoardStatusDTO;
import br.com.codr.realtimechecker.models.dto.MessageDTO;
import br.com.codr.realtimechecker.models.dto.MessageType;
import br.com.codr.realtimechecker.services.BoardsService;
import br.com.codr.realtimechecker.services.CheckerGameProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Service
public class BoardHandler extends TextWebSocketHandler {

    private final BoardsService boardsService;
    private final CheckerGameProducer checkerGameProducer;

    private final WebSocketSessions webSocketSessions;

    public BoardHandler(BoardsService boardsService, CheckerGameProducer checkerGameProducer, WebSocketSessions webSocketSessions) {
        this.boardsService = boardsService;
        this.checkerGameProducer = checkerGameProducer;
        this.webSocketSessions = webSocketSessions;
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession currentSession, TextMessage message) throws Exception {

        System.out.println("message.getPayload() " + message.getPayload());

        final var objectMapper = new ObjectMapper();
        final var messageDTO = objectMapper.readValue(message.getPayload(), MessageDTO.class);

        if (messageDTO.getType().equals(MessageType.CREATE_NEW_GAME)) {

            final var board = boardsService.create(messageDTO.getUserId(), currentSession.getId(), messageDTO.getContent().getBlackPositions());

            final var boardStatusDTO = BoardStatusDTO.fromEntity(board);
            final var response = objectMapper.writeValueAsString(boardStatusDTO);
            final var resp = new TextMessage(response);
            currentSession.sendMessage(resp);

        } else if (messageDTO.getType().equals(MessageType.ENTER_A_GAME)) {

            final var board = boardsService.addOtherPlayer(messageDTO.getCode(), messageDTO.getUserId(), currentSession.getId(), messageDTO.getContent().getWhitePositions());

            final var boardStatusDTO = BoardStatusDTO.fromEntity(board);
            final var response = objectMapper.writeValueAsString(boardStatusDTO);
            final var resp = new TextMessage(response);
            currentSession.sendMessage(resp);
            sendBoardMessageToOtherPlayer(response);

        } else if (messageDTO.getType().equals(MessageType.CONTENT_CHANGE)) {

            boardsService.findById(messageDTO.getId())
                .ifPresent(board -> {
                    boardsService.updatePositions(board, messageDTO.getContent().getWhitePositions(), messageDTO.getContent().getBlackPositions());
                    sendBoardMessageToOtherPlayer(message.getPayload());
                });
        } else if (messageDTO.getType().equals(MessageType.SEND_CHAT_MESSAGE)) {
            sendTextMessageToOtherPlayer(message.getPayload());
        }
    }

    private void sendTextMessageToOtherPlayer(String payload) {
        checkerGameProducer.sendBoard(payload);
    }

    private void sendBoardMessageToOtherPlayer(String messagePayload) {
        sendMessage(messagePayload);
    }

    private void sendMessage(String messagePayload) {
        checkerGameProducer.sendBoard(messagePayload);
    }

    public void sendMessageToAll(WebSocketSession currentSession, String message) throws IOException {
        for (WebSocketSession sess : webSocketSessions.sessions) {
            if (!sess.getId().equals(currentSession.getId()))
                sess.sendMessage(new TextMessage(message));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketSessions.sessions.remove(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessions.sessions.add(session);
    }


}
