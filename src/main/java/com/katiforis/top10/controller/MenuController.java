package com.katiforis.top10.controller;

import com.katiforis.top10.DTO.*;
import com.katiforis.top10.DTO.request.FindGame;
import com.katiforis.top10.DTO.request.GetRank;
import com.katiforis.top10.DTO.response.*;
import com.katiforis.top10.model.Player;
import com.katiforis.top10.service.GameHandlerService;
import com.katiforis.top10.service.PlayerService;
import com.katiforis.top10.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@MessageMapping("/menu")
public class MenuController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
    GameHandlerService gameHandlerService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/login")
	ResponseEntity login(PlayerDto playerDto) {
		log.debug("Start PlayerController.login");
		Player player = playerService.login(playerDto.getPlayerId(), playerDto.getUsername());
		Profile profile = new Profile();
		profile.setUsername(player.getUsername());
		ResponseEntity<Profile> response = new ResponseEntity<>(profile, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(player.getPlayerId(), Constants.MAIN_TOPIC, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@MessageMapping("/rank")
	ResponseEntity getRankList(GetRank get) {
		log.debug("Start PlayerController.getRankList");
		List<Player> players = playerService.getPlayers(0, 10);
		RankList rankList = new RankList();
		rankList.setPlayers(modelMapper.map(players,  new TypeToken<List<PlayerDto>>(){}.getType()));
		rankList.setUserId(get.getPlayerId());
		ResponseEntity<RankList> response = new ResponseEntity<>(rankList, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(get.getPlayerId(), Constants.MAIN_TOPIC, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@MessageMapping("/game/find")
	public void findGame(FindGame findGame) {
		log.debug("Start GameController.findGame");
		gameHandlerService.findGame(findGame);
		log.debug("End GameController.findGame");
	}
}
