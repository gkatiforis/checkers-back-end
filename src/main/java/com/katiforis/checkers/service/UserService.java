package com.katiforis.checkers.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.katiforis.checkers.DTO.request.Reward;
import com.katiforis.checkers.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    User registerWithGoogle(String userId, GoogleIdToken token);

    User registerGuest(String userId);

    User getUser(String userId);

    long deleteUser(String userId);

    User getGuestUser(String userId);

    List<User> getPlayers(int page, int size);

    long getPlayerPosition(User user);

    void addReward(Reward reward);
}
