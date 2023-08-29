package br.com.codr.realtimechecker.models.dto;

import br.com.codr.realtimechecker.models.entities.Board;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BoardStatusDTO {

    private String id;
    private Board.Status status;

    private String code;

    public static BoardStatusDTO fromEntity(Board board) {
        final var boardStateDTO = new BoardStatusDTO();
        boardStateDTO.id = board.getId();
        boardStateDTO.status = board.getStatus();
        boardStateDTO.code = board.getCode();
        return boardStateDTO;
    }

}
