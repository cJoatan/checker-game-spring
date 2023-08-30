package br.com.codr.realtimechecker.services;

import br.com.codr.realtimechecker.models.entities.Board;
import br.com.codr.realtimechecker.models.entities.GameUser;
import br.com.codr.realtimechecker.repositories.BoardsRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BoardsService {

    private final BoardsRepository boardsRepository;

    public Board create(String creatorId, String sessionId) {
        String code = RandomStringUtils.random(6, true, false);

        final var gameUser = new GameUser();
        gameUser.setId(creatorId);
        gameUser.setSessionId(sessionId);

        final var board = new Board();
        board.setPlayerBlack(gameUser);
        board.setCode(code);
        board.setStatus(Board.Status.WAITING_OPPONENT);

        return boardsRepository.save(board);
    }

    public Board addOtherPlayer(String code, String otherPlayerId, String sessionId) {

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
}
