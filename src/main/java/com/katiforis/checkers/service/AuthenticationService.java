package com.katiforis.checkers.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;


public interface AuthenticationService {

    UsernamePasswordAuthenticationToken authenticate(final String userId, final String token) throws AuthenticationException;

    UsernamePasswordAuthenticationToken authenticateGuest(String userId) throws AuthenticationException;

    UsernamePasswordAuthenticationToken generateAuthenticationToken(String userId);
}
