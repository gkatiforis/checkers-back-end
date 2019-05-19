package com.katiforis.top10.service.impl;

import com.katiforis.top10.model.Player;
import com.katiforis.top10.repository.PlayerRepository;
import com.katiforis.top10.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public Player login(String playerId, String username) {
		log.debug("Start PlayerServiceImpl.login");
		Player player = playerRepository.findByPlayerId(playerId);

		if (player == null) {
			player = new Player(playerId, username);
			playerRepository.save(player);
		}

		log.debug("End PlayerServiceImpl.login");
		return player;
	}

	public List<Player> getPlayers(int page, int size){
		Page<Player> players = playerRepository.findAllByOrderByPlayerDetails_EloDesc(new PageRequest(0, 10));
		return players.getContent();
	}
}
