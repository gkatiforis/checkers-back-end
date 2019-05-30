package com.katiforis.top10.config;

import com.katiforis.top10.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
    public static final String HEADER_TOKEN = "TOKEN";
    public static final String HEADER_USER_ID = "USER_ID";

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) throws AuthenticationException {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String token = accessor.getFirstNativeHeader(HEADER_TOKEN);
            String userId = accessor.getFirstNativeHeader(HEADER_USER_ID);

            if (token != null && (token.equals("null") || token.trim().isEmpty())) {
                token = null;
            }
            if (userId != null && (userId.equals("null") || userId.trim().isEmpty())) {
                userId = null;
            }
            UsernamePasswordAuthenticationToken user;

                if (token != null) {
                    user = authenticationService.authenticate(userId, token);
                } else {
                    user = authenticationService.authenticateGuest(userId);
                }
                accessor.setUser(user);

        }
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel messageChannel, boolean b) {
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel messageChannel, boolean b, Exception e) {
    }

    @Override
    public boolean preReceive(MessageChannel messageChannel) {
        return false;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel messageChannel) {
        return null;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel messageChannel, Exception e) {
    }
}