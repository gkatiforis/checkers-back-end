package com.katiforis.top10.controller;

import com.katiforis.top10.DTO.*;
import com.katiforis.top10.DTO.request.FindGame;
import com.katiforis.top10.DTO.request.GetRank;
import com.katiforis.top10.DTO.response.*;
import com.katiforis.top10.model.Player;
import com.katiforis.top10.repository.PlayerRepository;
import com.katiforis.top10.services.GameService;
import com.katiforis.top10.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@MessageMapping("/menu")
public class PlayerController {

	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	GameService gameService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/login")
	ResponseEntity login(PlayerDto playerDTO) {
		log.debug("Start PlayerController.login");

		Player player = playerRepository.findByPlayerId(playerDTO.getPlayerId());

		if (player == null) {
			player = new Player( playerDTO.getPlayerId(), playerDTO.getUsername());
			playerRepository.save(player);
		}

		log.debug("End PlayerController.login");

		Profile profile = new Profile();
		profile.setUsername(player.getUsername());
		ResponseEntity<Profile> response = new ResponseEntity<>(profile, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(player.getPlayerId(), Constants.MAIN_TOPIC, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@MessageMapping("/rank")
	ResponseEntity getRankList(GetRank get) {
		log.debug("Start PlayerController.getRankList");
		Page<Player> rankPage = playerRepository.findAllByOrderByPlayerDetails_EloDesc(new PageRequest(0, 10));
		RankList rankList = new RankList();
		rankList.setPlayers(modelMapper.map(rankPage.getContent(),  new TypeToken<List<PlayerDto>>(){}.getType()));
		rankList.setUserId(get.getPlayerId());
		ResponseEntity<RankList> response = new ResponseEntity<>(rankList, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(get.getPlayerId(), Constants.MAIN_TOPIC, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@MessageMapping("/game/find")
	public void findGame(FindGame findGame) {
		log.debug("Start GameController.findGame");
		gameService.findGame(findGame);
		log.debug("End GameController.findGame");
	}
}
