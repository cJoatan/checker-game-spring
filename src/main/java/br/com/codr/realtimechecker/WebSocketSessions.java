package br.com.codr.realtimechecker;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Service
public class WebSocketSessions {

    public final Set<WebSocketSession> sessions = new HashSet<>();

}
