package br.com.codr.realtimechecker.services;

import br.com.codr.realtimechecker.models.entities.Board;
import br.com.codr.realtimechecker.models.entities.GameUser;
import br.com.codr.realtimechecker.models.entities.TextMessage;
import br.com.codr.realtimechecker.repositories.BoardsRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BoardsService {

    private final BoardsRepository boardsRepository;

    public Board create(String creatorId, String sessionId, List<List<Integer>> positions) {
        String code = RandomStringUtils.random(6, true, false);

        final var gameUser = new GameUser();
        gameUser.setId(creatorId);
        gameUser.setSessionId(sessionId);
        gameUser.setPositions(positions);

        final var board = new Board();
        board.setPlayerBlack(gameUser);
        board.setCode(code);
        board.setStatus(Board.Status.WAITING_OPPONENT);

        return boardsRepository.save(board);
    }

    public Board addOtherPlayer(String code, String otherPlayerId, String sessionId, List<List<Integer>> otherPlayersPositions) {

        final var board = boardsRepository.findByCode(code);
        final var playerSearch = board.getPlayers()
                .stream()
                .filter(player -> player.getId().equals(otherPlayerId))
                .findFirst();

        if (playerSearch.isPresent()) {
            final var gameUser = playerSearch.get();
            gameUser.setSessionId(sessionId);
            return boardsRepository.save(board);
        } else {
            final var gameUser = new GameUser();
            gameUser.setId(otherPlayerId);
            gameUser.setSessionId(sessionId);
            gameUser.setPositions(otherPlayersPositions);

            board.setPlayerWhite(gameUser);
            board.setStatus(Board.Status.START_GAME);
            return boardsRepository.save(board);
        }

    }

    public void finishBoard(String id) {
//        boardsRepository.findBy
    }

    public Optional<Board> findById(String id) {
        return boardsRepository.findById(id);
    }

    public void removeSession(String sessionId) {

        boardsRepository.findByPlayersSessionId(sessionId)
            .ifPresent(board -> board.getPlayerBySession(sessionId)
                .ifPresent(gameUser -> {
                    gameUser.setSessionId(null);
                    boardsRepository.save(board);
                })
            );

    }

    public void updatePositions(Board board, List<List<Integer>> whitePositions, List<List<Integer>> blackPositions) {
        board.getPlayerWhite().setPositions(whitePositions);
        board.getPlayerBlack().setPositions(blackPositions);
        boardsRepository.save(board);
    }

    public Optional<TextMessage> addTextMessage(Board board, String sessionId, String text) {
        final var playerBySession = board.getPlayerBySession(sessionId);

        if (playerBySession.isPresent()) {

            final var gameUser = playerBySession.get();
            final Integer order = board.getTextMessages().size() + 1;
            final var textMessage = new TextMessage();
            textMessage.setText(text);
            textMessage.setGameUserId(gameUser.getId());
            textMessage.setOrder(Long.parseLong(order.toString()));

            board.getTextMessages().add(textMessage);
            boardsRepository.save(board);
            return Optional.of(textMessage);
        }
        return Optional.empty();
    }
}
