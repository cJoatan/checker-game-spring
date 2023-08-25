package br.com.codr.realtimechecker;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Principal;

@AllArgsConstructor
@Service
public class EchoController {

    private final EchoHandler echoHandler;

//    @Scheduled(cron = "*/1 * * * * *")
    public void sendMessage() throws IOException {
//        for (WebSocketSession session : echoHandler.sessions) {
//            session.sendMessage(new TextMessage("vaaaaa " + session.getId()));
//        }
    }

}
