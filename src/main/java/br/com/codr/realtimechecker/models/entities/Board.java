package br.com.codr.realtimechecker.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Document
public class Board {

    @Id
    private String id;

    private String code;

    private String playerWhite;

    private String playerBlack;

    private List<Integer> whitePositions;
    private List<Integer> blackPositions;

    private Status status;

    public List<String> getPlayers() {
        return List.of(playerWhite, playerBlack);
    }

    public Optional<String> getOtherPlayer(String currentPlayer) {

        return getPlayers()
            .stream()
            .filter(player -> !player.equals(currentPlayer))
            .findFirst();

    }

    public enum Status {

        WAITING_OPPONENT,
        START_GAME,
        PLAYING

    }



}
