package br.com.codr.realtimechecker.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Document
public class Board {

    @Id
    private String id;

    private String code;

    private GameUser playerWhite;

    private GameUser playerBlack;

    private Status status;

    private List<TextMessage> textMessages = new ArrayList<>();

    public List<GameUser> getPlayers() {
        final var list = new ArrayList<GameUser>();
        if (playerWhite != null)
            list.add(playerWhite);
        if (playerBlack != null)
            list.add(playerBlack);
        return list;

    }

    public Optional<GameUser> getOtherPlayer(String currentPlayerId) {

        return getPlayers()
            .stream()
            .filter(player -> !player.getId().equals(currentPlayerId))
            .findFirst();

    }

    public Optional<GameUser> getPlayerBySession(String sessionId) {
        return getPlayers()
            .stream()
            .filter(player -> player.getSessionId().equals(sessionId))
            .findFirst();
    }

    public enum Status {

        WAITING_OPPONENT,
        START_GAME,
        PLAYING

    }



}
