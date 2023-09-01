package br.com.codr.realtimechecker.models.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextMessage {

    private String gameUserId;

    private Long order;

    private String text;


}
