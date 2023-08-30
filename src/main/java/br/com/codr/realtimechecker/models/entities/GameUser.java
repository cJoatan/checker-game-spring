package br.com.codr.realtimechecker.models.entities;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
public class GameUser {

    @Id
    @NotNull
    private String id;

    private String sessionId;

    private List<List<Integer>> positions;

}
