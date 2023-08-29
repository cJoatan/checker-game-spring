package br.com.codr.realtimechecker;

import br.com.codr.realtimechecker.models.dto.BoardStatusDTO;
import br.com.codr.realtimechecker.models.dto.MessageDTO;
import br.com.codr.realtimechecker.models.dto.MessageType;
import br.com.codr.realtimechecker.models.entities.Board;
import br.com.codr.realtimechecker.services.BoardsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class BoardHandler extends TextWebSocketHandler {

    private final BoardsService boardsService;

    final Set<WebSocketSession> sessions = new HashSet<>();

    public BoardHandler(BoardsService boardsService) {
        this.boardsService = boardsService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession currentSession, TextMessage message) throws Exception {

        System.out.println("message.getPayload() " + message.getPayload());

        final var objectMapper = new ObjectMapper();
        final var messageDTO = objectMapper.readValue(message.getPayload(), MessageDTO.class);

        if (messageDTO.getType().equals(MessageType.CREATE_NEW_GAME)) {

            final var board = boardsService.create(currentSession.getId());

            final var boardStatusDTO = BoardStatusDTO.fromEntity(board);
            final var response = objectMapper.writeValueAsString(boardStatusDTO);
            final var resp = new TextMessage(response);
            currentSession.sendMessage(resp);

        } else if (messageDTO.getType().equals(MessageType.ENTER_A_GAME)) {

            final var board = boardsService.addOtherPlayer(messageDTO.getCode(), currentSession.getId());

            final var boardStatusDTO = BoardStatusDTO.fromEntity(board);
            final var response = objectMapper.writeValueAsString(boardStatusDTO);
            final var resp = new TextMessage(response);
            currentSession.sendMessage(resp);

        } else if (messageDTO.getType().equals(MessageType.CONTENT_CHANGE)) {

            boardsService.findById(messageDTO.getId())
                .ifPresent(board -> sendMessageToOtherPlayer(board, currentSession, message.getPayload()));
        }
    }

    private void sendMessageToOtherPlayer(Board board, WebSocketSession currentSession, String messagePayload) {
        board.getOtherPlayer(currentSession.getId())
            .ifPresent(otherPlayer -> sendMessage(otherPlayer, messagePayload));
    }

    private void sendMessage(String sessionId, String messagePayload) {
        sessions.stream()
            .filter(session -> session.getId().equals(sessionId))
            .findFirst()
            .ifPresent(session -> {
                try {
                    session.sendMessage(new TextMessage(messagePayload));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

    public void sendMessageToAll(WebSocketSession currentSession, String message) throws IOException {
        for (WebSocketSession sess : sessions) {
            if (!sess.getId().equals(currentSession.getId()))
                sess.sendMessage(new TextMessage(message));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

//        boardsService.finishBoard(session.getId());
        System.out.println("closing connection");
        sessions.remove(session);

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }


}