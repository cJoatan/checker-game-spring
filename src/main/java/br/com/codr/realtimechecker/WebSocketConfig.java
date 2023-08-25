package br.com.codr.realtimechecker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(echoHandler(), "/echoHandler").setAllowedOriginPatterns("*").withSockJS();
        registry.addHandler(echoHandler(), "/echoHandler").setAllowedOriginPatterns("*");
    }

    @Bean
    public EchoHandler echoHandler() {
        return new EchoHandler();
    }
}
