package br.com.codr.realtimechecker.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessageContent {

    private List<List<Integer>> whitePositions;
    private List<List<Integer>> blackPositions;

}
