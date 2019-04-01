package com.katiforis.top10.controller;

import com.katiforis.top10.DTO.game.PlayerDTO;
import com.katiforis.top10.model.Player;
import com.katiforis.top10.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@MessageMapping("/player")
public class PlayerController {

	@Autowired
	PlayerRepository playerRepository;

	@MessageMapping("/login")
	ResponseEntity login(PlayerDTO playerDTO) {
		log.debug("Start PlayerController.login");

		Player player = playerRepository.findByPlayerId(playerDTO.getPlayerId());

		if (player == null) {
			Player newPlayer = new Player( playerDTO.getPlayerId(), playerDTO.getUsername());
			playerRepository.save(newPlayer);
		}

		log.debug("End PlayerController.login");
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
