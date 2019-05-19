package com.katiforis.top10.service;

import com.katiforis.top10.model.Player;

import java.util.List;

public interface PlayerService {

    Player login(String playerId, String username);

    List<Player> getPlayers(int page, int size);
}

