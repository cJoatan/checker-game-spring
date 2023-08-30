package br.com.codr.realtimechecker.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {

    private String id;

    private String userId;

    private String type;

    private MessageContent content;

    private String code;

}
