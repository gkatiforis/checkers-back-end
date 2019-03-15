package com.katiforis.top10.controller;


import com.katiforis.top10.DTO.game.PlayerDTO;
import com.katiforis.top10.model.Player;
import com.katiforis.top10.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@MessageMapping("/player")
public class PlayerController {

	public static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

	@Autowired
	PlayerRepository playerRepository;

	@MessageMapping("/login")
	ResponseEntity login(PlayerDTO playerDTO) {
		logger.debug("Start PlayerController.login");

		Player player = playerRepository.findByPlayerId(playerDTO.getPlayerId());

		if (player == null) {
			Player newPlayer = new Player( playerDTO.getPlayerId(), playerDTO.getUsername());
			playerRepository.save(newPlayer);
		}

		logger.debug("End PlayerController.login");
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
