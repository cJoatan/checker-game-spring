package br.com.codr.realtimechecker.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextMessageDTO {

    private String type;

    private String gameUserId;

    private Long order;

    private String text;

}
