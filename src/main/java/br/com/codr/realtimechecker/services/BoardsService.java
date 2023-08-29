package br.com.codr.realtimechecker.services;

import br.com.codr.realtimechecker.models.entities.Board;
import br.com.codr.realtimechecker.repositories.BoardsRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BoardsService {

    private final BoardsRepository boardsRepository;

    public Board create(String creatorId) {
        String code = RandomStringUtils.random(6, true, false);
        final var board = new Board();
        board.setPlayerBlack(creatorId);
        board.setCode(code);
        board.setStatus(Board.Status.WAITING_OPPONENT);
        return boardsRepository.save(board);
    }

    public Board addOtherPlayer(String code, String otherPlayerId) {
        System.out.println("code " + code);
        final var board = boardsRepository.findByCode(code);
        board.setPlayerWhite(otherPlayerId);
        board.setStatus(Board.Status.START_GAME);
        return boardsRepository.save(board);
    }

    public void finishBoard(String id) {
//        boardsRepository.findBy
    }

    public Optional<Board> findById(String id) {
        return boardsRepository.findById(id);
    }
}
