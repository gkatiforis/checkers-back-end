package com.katiforis.checkers.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.katiforis.checkers.model.User;
import com.katiforis.checkers.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.katiforis.checkers.service.impl.UserServiceImpl.GUEST_ID_LENGTH;
import static com.katiforis.checkers.service.impl.UserServiceImpl.GUEST_ID_PREFIX;

@Slf4j
@Component
public class AuthenticationService {

    @Value("${google.web.client.id}")
    private String webClientId;

    @Autowired
    private UserService userService;

    public UsernamePasswordAuthenticationToken authenticate(final String userId, final String token) throws AuthenticationException {
        User user;
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new AuthenticationCredentialsNotFoundException("Token was null or empty.");
            }
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(webClientId))
                    .build();
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                user = userService.getUser(payload.getSubject());
                if(user == null){
                    user = userService.registerWithGoogle(userId, idToken);
                }
            } else {
                throw new BadCredentialsException("Invalid login token");
            }
        } catch (Exception e) {
            log.error("Error: " + e);
            throw new BadCredentialsException("Bad credentials: ",  e);
        }

       return generateAuthenticationToken(user.getUserId());
    }

    public UsernamePasswordAuthenticationToken authenticateGuest(String userId) throws AuthenticationException {
        User user;
        if(userId == null){
            userId = GUEST_ID_PREFIX + Utils.getRandomString(GUEST_ID_LENGTH - GUEST_ID_PREFIX.length());
            user = userService.registerGuest(userId);
        }else{
            user = userService.getGuestUser(userId);
            if(user == null) {
                user = userService.registerGuest(userId);
            }
        }

        return generateAuthenticationToken(user.getUserId());
    }

    private UsernamePasswordAuthenticationToken generateAuthenticationToken(String userId){
        return new UsernamePasswordAuthenticationToken(
                userId,
                null,
                Collections.singleton((GrantedAuthority) () -> "USER"));
    }
}
