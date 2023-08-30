package br.com.codr.realtimechecker.models.dto;

import br.com.codr.realtimechecker.models.entities.Board;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class BoardStatusDTO {

    private String id;
    private Board.Status status;

    private String code;

    private MessageContent content;


    public static BoardStatusDTO fromEntity(Board board) {
        final var boardStateDTO = new BoardStatusDTO();
        boardStateDTO.id = board.getId();
        boardStateDTO.status = board.getStatus();
        boardStateDTO.code = board.getCode();

        final var messageContent = new MessageContent();
        if (board.getPlayerBlack() != null && board.getPlayerBlack().getPositions() != null)
            messageContent.setBlackPositions(board.getPlayerBlack().getPositions());

        if (board.getPlayerWhite() != null && board.getPlayerWhite().getPositions() != null)
            messageContent.setWhitePositions(board.getPlayerWhite().getPositions());

        boardStateDTO.content = messageContent;

        return boardStateDTO;
    }

}
